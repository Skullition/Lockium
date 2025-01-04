package dev.skullition.lockium.service.command.selfrole;

import dev.skullition.lockium.model.entity.AllowedSelfRole;
import dev.skullition.lockium.model.entity.SelfRole;
import dev.skullition.lockium.repository.AllowedSelfRoleRepository;
import dev.skullition.lockium.repository.SelfRoleRepository;
import io.github.freya022.botcommands.api.commands.annotations.BotPermissions;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import java.awt.Color;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Command relating to creating or updating a member's self roles. */
@Command
public class SlashUpdate extends ApplicationCommand {
  private static final Logger logger = LoggerFactory.getLogger(SlashUpdate.class);
  private static final ExecutorService VIRTUAL_EXECUTOR =
      Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("Commands").factory());
  private final AllowedSelfRoleRepository allowedSelfRoleRepository;
  private final SelfRoleRepository selfRoleRepository;

  /** Command relating to creating or updating a member's self roles. */
  public SlashUpdate(
      AllowedSelfRoleRepository allowedSelfRoleRepository, SelfRoleRepository selfRoleRepository) {
    this.allowedSelfRoleRepository = allowedSelfRoleRepository;
    this.selfRoleRepository = selfRoleRepository;
  }

  @NotNull
  private Role createRole(@NotNull Guild guild, @NotNull User user) {
    String name = user.getGlobalName() == null ? user.getName() : user.getGlobalName();
    Role role = guild.createRole().setName(name).complete();

    List<AllowedSelfRole> guildAllowedSelfRoles =
        allowedSelfRoleRepository.findByGuildId(guild.getIdLong());
    Role referenceRole = guild.getRoleById(guildAllowedSelfRoles.getFirst().getId());
    if (referenceRole == null) {
      throw new IllegalStateException(
          "reference role of id:%s in guild:%s not found."
              .formatted(guildAllowedSelfRoles.getFirst().getId(), guild.getName()));
    }
    final int allowedSelfRolePosition = referenceRole.getPosition();

    guild.modifyRolePositions().selectPosition(role).moveTo(allowedSelfRolePosition + 1).complete();

    guild.addRoleToMember(user, role).queue();
    selfRoleRepository.save(new SelfRole(role.getName(), role.getIdLong()));
    return role;
  }

  @NotNull
  private Role getOrCreateSelfRole(
      @NotNull Guild guild, @NotNull User user, @NotNull List<Long> memberRoleIds) {
    SelfRole selfRole = selfRoleRepository.findByIdIn(memberRoleIds);
    logger.debug("Selfrole: {}, memberRoleIds: {}", selfRole, memberRoleIds);

    if (selfRole == null) {
      return createRole(guild, user);
    }
    Role role = guild.getRoleById(selfRole.getId());
    if (role == null) {
      return createRole(guild, user);
    }
    return role;
  }

  /**
   * Handles the {@code /self_role update color} command to create or update the user's role and
   * update its color.
   */
  @BotPermissions(Permission.MANAGE_ROLES)
  @JDASlashCommand(
      name = "self_role",
      group = "update",
      subcommand = "color",
      description = "Update your role's color.")
  public void onSlashUpdate(
      GuildSlashEvent event,
      @NotNull @SlashOption(name = "hex_color", description = "Which color to update to.")
          String stringColor) {
    List<Long> memberRoleIds =
        event.getMember().getRoles().stream().map(ISnowflake::getIdLong).toList();
    if (!allowedSelfRoleRepository.existsByIdIn(memberRoleIds)) {
      event.reply("You do not have any of the required roles!").setEphemeral(true).queue();
      return;
    }
    if (!event.getGuild().getSelfMember().canInteract(event.getMember())) {
      event
          .reply("Lockium cannot interact with you. Try moving one of Lockium's roles higher.")
          .setEphemeral(true)
          .queue();
      return;
    }

    VIRTUAL_EXECUTOR.submit(
        () -> {
          final var role = getOrCreateSelfRole(event.getGuild(), event.getUser(), memberRoleIds);
          final Color color;
          try {
            color = Color.decode(stringColor);
          } catch (NumberFormatException e) {
            event.reply("Invalid color!").setEphemeral(true).queue();
            return;
          }
          role.getManager().setColor(color).complete();

          event
              .reply("Your role color has been updated to %s.".formatted(stringColor))
              .setEphemeral(true)
              .complete();
        });
  }
}
