package dev.skullition.lockium.service.command.owner;

import dev.skullition.lockium.model.ClothingEffects;
import dev.skullition.lockium.model.GrowtopiaItem;
import dev.skullition.lockium.proxy.GrowtopiaWikiProxy;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.CommandScope;
import io.github.freya022.botcommands.api.commands.application.annotations.Test;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/** Command to update item description based on attached file. */
@Command
public class SlashUpdateItemData extends ApplicationCommand {
  private static final Logger logger = LoggerFactory.getLogger(SlashUpdateItemData.class);
  private final GrowtopiaWikiProxy proxy;

  @Value("${botcommands.core.predefined-owner-ids}")
  private Set<Long> owners;

  public SlashUpdateItemData(GrowtopiaWikiProxy proxy) {
    this.proxy = proxy;
  }

  /**
   * Handles the {@code /owner update_item_data} command. Sends a file with the required item data.
   *
   * @param event the {@link GuildSlashEvent} representing the slash command interaction
   * @param attachment the file with the required item names, separated by '|'
   */
  @Test
  @TopLevelSlashCommandData(
      description = "Owner only slash commands.",
      defaultLocked = true,
      scope = CommandScope.GUILD)
  @JDASlashCommand(
      name = "owner",
      subcommand = "update_item_data",
      description = "Updates all item data from a file.")
  public void onSlashUpdate(
      GuildSlashEvent event,
      @NotNull @SlashOption(name = "file", description = "File to update from")
          Message.Attachment attachment) {
    if (!owners.contains(event.getUser().getIdLong())) {
      logger.warn(
          "{} tried to execute {} without enough privilege",
          event.getUser(),
          event.getFullCommandName());
      event.reply("This is an owner-only command.").queue();
      return;
    }

    attachment
        .getProxy()
        .download()
        .thenApply(this::processItemDescriptionsFromStream)
        .thenAccept(
            fileUploads -> {
              if (fileUploads.isEmpty()) {
                event.getHook().editOriginal("Couldn't get wiki entry for an item.").queue();
                return;
              }
              var message = MessageCreateData.fromFiles(fileUploads);
              event.getHook().sendMessage(message).queue();
            });

    event.reply("Updating item data from " + attachment.getFileName()).queue();
  }

  private List<FileUpload> processItemDescriptionsFromStream(InputStream stream) {
    StringBuilder descriptionSb = new StringBuilder();
    StringBuilder chiSb = new StringBuilder();
    StringBuilder effectsSb = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        String[] split = line.split("\\|");

        String itemId = split[0];
        String itemName = split[1];
        logger.debug("Getting item data of {} ({})", itemName, itemId);
        var result = proxy.getItemData(itemName);
        if (result.isEmpty()) {
          logger.error("Item {} not found while trying to look up wiki.", itemName);
          return Collections.emptyList();
        }

        GrowtopiaItem item = result.get();

        descriptionSb.append("// ").append(itemName).append("\n");
        descriptionSb.append(itemId).append("|").append(item.description()).append("\n");
        descriptionSb.append("\n");

        chiSb.append("// ").append(itemName).append("\n");
        chiSb.append(itemId).append("|").append(item.itemField().chi()).append("\n");
        chiSb.append("\n");

        ClothingEffects effects = item.clothingEffects();
        if (effects == null) {
          logger.debug("Skipping {} as it is not clothing.", itemName);
        } else {
          effectsSb.append("// ").append(itemName).append("\n");
          effectsSb
              .append(itemId)
              .append("|")
              .append(effects.effect())
              .append("|")
              .append(effects.onWearingText())
              .append(effects.onRemoveText())
              .append("\n");
          effectsSb.append("\n");
        }
      }
    } catch (IOException e) {
      logger.error("Couldn't read item data file", e);
      return Collections.emptyList();
    }
    // TODO: Update all item stuff instead
    FileUpload descriptions =
        FileUpload.fromData(descriptionSb.toString().getBytes(), "Item Descriptions.txt");
    FileUpload chi = FileUpload.fromData(chiSb.toString().getBytes(), "Chi.txt");
    FileUpload effects = FileUpload.fromData(effectsSb.toString().getBytes(), "Effects.txt");
    return List.of(descriptions, chi, effects);
  }
}
