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
import net.dv8tion.jda.api.entities.Message;
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
  public void onSlashUpdateColor(
      GuildSlashEvent event,
      @NotNull @SlashOption(name = "hex_color", description = "Which color to update to.")
          String stringColor) {
    List<Long> memberRoleIds =
        event.getMember().getRoles().stream().map(ISnowflake::getIdLong).toList();
    if (checkMemberEligible(event, memberRoleIds)) {
      return;
    }

    VIRTUAL_EXECUTOR.submit(
        () -> {
          try {
            final var role = getOrCreateSelfRole(event.getGuild(), event.getUser(), memberRoleIds);
            final Color color;
            try {
              color = Color.decode(stringColor);
            } catch (NumberFormatException e) {
              event
                  .reply(
                      "%s is an invalid color. Example of valid color: `#fffff`"
                          .formatted(stringColor))
                  .setEphemeral(true)
                  .queue();
              return;
            }
            role.getManager().setColor(color).complete();

            event
                .reply("Your role color has been updated to %s.".formatted(stringColor))
                .setEphemeral(true)
                .complete();
          } catch (Throwable e) {
            event.reply("Something went wrong! Please try again later!").queue();
            logger.error("Error caught while trying to update role color.", e);
          }
        });
  }

  private boolean checkMemberEligible(GuildSlashEvent event, List<Long> memberRoleIds) {
    if (!allowedSelfRoleRepository.existsByIdIn(memberRoleIds)) {
      event.reply("You do not have any of the required roles!").setEphemeral(true).queue();
      return true;
    }
    if (!event.getGuild().getSelfMember().canInteract(event.getMember())) {
      event
          .reply("Lockium cannot interact with you. Try moving one of Lockium's roles higher.")
          .setEphemeral(true)
          .queue();
      return true;
    }
    return false;
  }

  /**
   * Handles the {@code /self_role update icon} command to create or update the user's role and
   * update its icon.
   */
  @BotPermissions(Permission.MANAGE_ROLES)
  @JDASlashCommand(
      name = "self_role",
      group = "update",
      subcommand = "icon",
      description = "Update your role's icon.")
  public void onSlashUpdateIcon(
      GuildSlashEvent event,
      @NotNull @SlashOption(name = "icon", description = "Which icon to update to.")
          Message.Attachment attachment) {
    List<Long> memberRoleIds =
        event.getMember().getRoles().stream().map(ISnowflake::getIdLong).toList();
    if (checkMemberEligible(event, memberRoleIds)) {
      return;
    }
    if (!attachment.isImage()) {
      event.reply("This is not an image.").setEphemeral(true).queue();
      return;
    }
    VIRTUAL_EXECUTOR.submit(
        () -> {
          try {
            final var role = getOrCreateSelfRole(event.getGuild(), event.getUser(), memberRoleIds);
            final var icon = attachment.getProxy().downloadAsIcon().get();
            role.getManager().setIcon(icon).complete();

            event.reply("Your role icon has been updated.").setEphemeral(true).complete();

          } catch (Throwable e) {
            event.reply("Something went wrong! Please try again later!").queue();
            logger.error("Error caught while trying to update role icon.", e);
          }
        });
  }
}
