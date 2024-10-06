package dev.skullition.lockium.service.command.owner;

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
import java.nio.charset.StandardCharsets;
import java.util.Optional;
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
public class SlashUpdateItemDescription extends ApplicationCommand {
  private static final Logger logger = LoggerFactory.getLogger(SlashUpdateItemDescription.class);
  private final GrowtopiaWikiProxy proxy;

  @Value("${botcommands.core.predefined-owner-ids}")
  private Set<Long> owners;

  public SlashUpdateItemDescription(GrowtopiaWikiProxy proxy) {
    this.proxy = proxy;
  }

  /**
   * Handles the {@code /owner update_item_descriptions} command. Sends a file with the required
   * item descriptions.
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
      subcommand = "update_item_descriptions",
      description = "Updates all item descriptions from a file.")
  public void onSlashUpdate(
      GuildSlashEvent event,
      @NotNull @SlashOption(name = "file", description = "File to update from")
          Message.Attachment attachment) {
    if (!owners.contains(event.getUser().getIdLong())) {
      event.reply("This is an owner-only command.").queue();
      return;
    }

    attachment
        .getProxy()
        .download()
        .thenApply(this::processItemDescriptionsFromStream)
        .thenAccept(
            optionalStream ->
                optionalStream.ifPresentOrElse(
                    bytes -> {
                      var fileUpload = FileUpload.fromData(bytes, "Item Descriptions.txt");
                      var message = MessageCreateData.fromFiles(fileUpload);
                      event.getHook().sendMessage(message).queue();
                    },
                    () ->
                        event
                            .getHook()
                            .editOriginal("Couldn't get wiki entry for an item.")
                            .queue()));

    event.reply("Updating the item descriptions from " + attachment.getFileName()).queue();
  }

  private Optional<byte[]> processItemDescriptionsFromStream(InputStream stream) {
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        String[] split = line.split("\\|");

        String itemId = split[0];
        String itemName = split[1];
        logger.debug("Getting item description of {} ({})", itemName, itemId);
        var result = proxy.getItemData(itemName);
        if (result.isEmpty()) {
          logger.error("Item {} not found while trying to look up wiki.", itemName);
          return Optional.empty();
        }

        GrowtopiaItem item = result.get();
        stringBuilder.append("// ").append(itemName).append("\n");
        stringBuilder.append(itemId).append("|").append(item.description()).append("\n");
        stringBuilder.append("\n");
      }
    } catch (IOException e) {
      logger.error("Couldn't read item description file", e);
      return Optional.empty();
    }
    byte[] bytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    return Optional.of(bytes);
  }
}
