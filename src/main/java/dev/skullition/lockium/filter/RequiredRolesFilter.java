package dev.skullition.lockium.filter;

import dev.skullition.lockium.annotation.RequiredRoles;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommandFilter;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommandInfo;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

/** Filter to limit command usage to only members with certain role(s). */
public class RequiredRolesFilter implements ApplicationCommandFilter<String> {
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RequiredRolesFilter.class);

  @NotNull
  private static List<Role> getMissingRoles(
      @NotNull RequiredRoles annotation, @NotNull Member member) {
    final var guild = member.getGuild();
    return Arrays.stream(annotation.value())
        .mapToObj(
            id -> {
              final var role = guild.getRoleById(id);
              if (role == null) {
                logger.warn("Could not find role with ID {} in guild {}", id, guild);
              }

              return role;
            })
        .filter(Objects::nonNull)
        .filter(r -> !member.getRoles().contains(r))
        .toList();
  }

  @Override
  public boolean getGlobal() {
    return false;
  }

  /**
   * Returns null if this filter should allow the command to run.
   *
   * <p>The object will be passed to your [ApplicationCommandRejectionHandler] if the command is
   * rejected.
   */
  @Nullable
  @Override
  public String check(
      @NotNull GenericCommandInteractionEvent event, @NotNull ApplicationCommandInfo commandInfo) {
    final var annotation = commandInfo.findAnnotation(RequiredRoles.class);
    if (annotation == null) {
      return null;
    }

    final var member = event.getMember();
    if (member == null) {
      logger.warn(
          "@RequiredRoles can only be used on guild-only commands: {}", commandInfo.getFunction());
      return "You are missing required roles";
    }

    final var missingRoles = getMissingRoles(annotation, member);
    if (!missingRoles.isEmpty()) {
      logger.trace(
          "Denied access to {} as they are missing required roles: {}", member, missingRoles);
      return "You are missing required roles";
    }

    return null;
  }
}
