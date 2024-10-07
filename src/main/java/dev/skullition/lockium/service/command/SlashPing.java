package dev.skullition.lockium.service.command;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;

/** Command to get current REST ping. */
@Command
public class SlashPing extends ApplicationCommand {
  /**
   * Handles the {@code /ping } command. Sends a response with the current ping.
   *
   * @param event the {@link GuildSlashEvent} representing the slash command interaction
   */
  @JDASlashCommand(name = "ping", description = "Gets the REST ping.")
  public void onSlashPing(GuildSlashEvent event) {
    event.deferReply(true).queue();

    event
        .getJDA()
        .getRestPing()
        .queue(ping -> event.getHook().editOriginal("Pong! Time taken: " + ping + " ms.").queue());
  }
}
