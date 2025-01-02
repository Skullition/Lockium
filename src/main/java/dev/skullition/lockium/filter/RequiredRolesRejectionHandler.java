package dev.skullition.lockium.filter;

import io.github.freya022.botcommands.api.commands.application.ApplicationCommandInfo;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommandRejectionHandler;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/** Handles rejections for when {@link RequiredRolesFilter} rejects the member. */
@Service
public class RequiredRolesRejectionHandler implements ApplicationCommandRejectionHandler<String> {
  @Override
  public void handle(
      @NotNull GenericCommandInteractionEvent event,
      @NotNull ApplicationCommandInfo commandInfo,
      @NotNull String userData) {
    event.reply(userData).setEphemeral(true).queue();
  }
}
