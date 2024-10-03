package dev.skullition.lockium.service.command.owner;

import dev.skullition.lockium.proxy.GrowtopiaWikiProxy;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

/**
* Command to update item description based on attached file.
*/
@Command
public class SlashUpdateItemDescription extends ApplicationCommand {
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
    event.reply(file.getFileName()).queue();
  }
}
