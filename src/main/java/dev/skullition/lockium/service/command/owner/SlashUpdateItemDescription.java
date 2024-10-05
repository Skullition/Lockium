package dev.skullition.lockium.service.command.owner;

import dev.skullition.lockium.model.GrowtopiaItem;
import dev.skullition.lockium.proxy.GrowtopiaWikiProxy;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Command to update item description based on attached file. */
@Command
public class SlashUpdateItemDescription extends ApplicationCommand {
  private static final Logger logger = LoggerFactory.getLogger(SlashUpdateItemDescription.class);
  private final GrowtopiaWikiProxy proxy;

  public SlashUpdateItemDescription(GrowtopiaWikiProxy proxy) {
    this.proxy = proxy;
  }

  @TopLevelSlashCommandData(description = "Owner only slash commands.")
  @JDASlashCommand(
      name = "owner",
      subcommand = "update_item_descriptions",
      description = "Updates all item descriptions from a file.")
  public void onSlashUpdate(
      GuildSlashEvent event,
      @NotNull @SlashOption(name = "file", description = "File to update from")
          Message.Attachment file) {
    file.getProxy().download().thenAccept(action -> consumeFile(action, event));
    event.reply("Updating the item descriptions from " + file.getFileName()).queue();
  }

  private void consumeFile(InputStream stream, GuildSlashEvent event) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        String[] split = line.split("\\|");
        
        String itemId = split[0];
        String itemName = split[1];
        logger.debug("Getting item description of {} ({})", itemName, itemId);
        var result = proxy.getItemData(itemName);
        if (result.isEmpty()) {
          event.getHook().editOriginal("Couldn't get wiki entry for " + itemName).queue();
          return;
        }
        
        GrowtopiaItem item = result.get();
        // TODO: Process obtained item desc
        logger.info("{} | {}", itemName, item.description());
      }
      stream.close();
    } catch (IOException e) {
      logger.error("Couldn't read item description file", e);
    }
  }
}
