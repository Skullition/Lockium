package dev.skullition.lockium.service.command.owner;

import dev.skullition.lockium.client.GrowtopiaWikiClient;
import dev.skullition.lockium.model.GrowtopiaWikiItem;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.CommandScope;
import io.github.freya022.botcommands.api.commands.application.annotations.Test;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import io.github.freya022.botcommands.api.core.BotOwners;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Command to update item description based on attached file. */
@Command
public class SlashUpdateItemData extends ApplicationCommand {
  private static final Logger logger = LoggerFactory.getLogger(SlashUpdateItemData.class);
  private final GrowtopiaWikiClient client;
  private final BotOwners owners;

  public SlashUpdateItemData(GrowtopiaWikiClient client, BotOwners owners) {
    this.client = client;
    this.owners = owners;
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
    if (!owners.isOwner(event.getUser())) {
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
    var descriptionsSb = new StringBuilder();
    var chiSb = new StringBuilder();
    var effectsSb = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        String[] split = line.split("\\|");

        String itemId = split[0];
        String itemName = split[1];
        logger.debug("Getting item data of {} ({})", itemName, itemId);
        var result = client.getItemData(itemName);
        if (result.isEmpty()) {
          logger.error("Item {} not found while trying to look up wiki.", itemName);
          return Collections.emptyList();
        }

        GrowtopiaWikiItem wikiItem = result.get();

        buildItemDataSection(
            itemName,
            stringBuilder ->
                stringBuilder
                    .append(itemId)
                    .append("|")
                    .append(wikiItem.description())
                    .append("\n"),
            descriptionsSb);
        buildItemDataSection(
            itemName,
            stringBuilder ->
                stringBuilder
                    .append(itemId)
                    .append("|")
                    .append(wikiItem.itemField().chi())
                    .append("\n"),
            chiSb);

        var effectsData = wikiItem.itemEffects();
        if (effectsData == null) {
          logger.debug("Skipping {} as it is not an item with effects.", itemName);
        } else {
          buildItemDataSection(
              itemName,
              stringBuilder ->
                  stringBuilder
                      .append(itemId)
                      .append("|")
                      .append(effectsData.effect())
                      .append("|")
                      .append(effectsData.onUseText())
                      .append("|")
                      .append(effectsData.onRemoveText())
                      .append("\n"),
              effectsSb);
        }
      }
    } catch (IOException e) {
      logger.error("Couldn't read item data file", e);
      return Collections.emptyList();
    }
    FileUpload descriptionsFile = createFileUpload(descriptionsSb, "Item Descriptions.txt");
    FileUpload chisFile = createFileUpload(chiSb, "Chi.txt");
    FileUpload effectsFile = createFileUpload(effectsSb, "Effects.txt");
    return List.of(descriptionsFile, chisFile, effectsFile);
  }

  private void buildItemDataSection(
      @NotNull String itemName,
      @NotNull Consumer<StringBuilder> data,
      @NotNull StringBuilder stringBuilder) {
    stringBuilder.append("// ").append(itemName).append("\n");
    data.accept(stringBuilder);
    stringBuilder.append("\n");
  }

  private FileUpload createFileUpload(StringBuilder data, String fileName) {
    return FileUpload.fromData(data.toString().getBytes(), fileName);
  }
}
