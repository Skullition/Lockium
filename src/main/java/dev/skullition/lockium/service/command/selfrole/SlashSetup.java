package dev.skullition.lockium.service.command.selfrole;

import dev.skullition.lockium.model.entity.AllowedSelfRole;
import dev.skullition.lockium.repository.AllowedSelfRoleRepository;
import io.github.freya022.botcommands.api.commands.annotations.BotPermissions;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.CommandScope;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import org.jetbrains.annotations.NotNull;

/** Command to set up which roles are allowed to create and update self roles. */
@Command
public class SlashSetup extends ApplicationCommand {
  private final AllowedSelfRoleRepository repository;

  /** Command to set up which roles are allowed to create and update self roles. */
  public SlashSetup(AllowedSelfRoleRepository repository) {
    this.repository = repository;
  }

  /**
   * Handles the {@code /self_role setup} command. Adds the role id through a repository interface.
   */
  @TopLevelSlashCommandData(
      scope = CommandScope.GUILD,
      contexts = InteractionContextType.GUILD,
      integrationTypes = IntegrationType.GUILD_INSTALL,
      defaultLocked = true,
      description = "Commands related to creating and updating self roles.")
  @BotPermissions(Permission.MANAGE_ROLES)
  @JDASlashCommand(
      name = "self_role",
      subcommand = "setup",
      description = "Sets up self roles for this guild.")
  public void onSlashSetup(
      GuildSlashEvent event,
      @NotNull @SlashOption(name = "role", description = "Which role to allow creating self roles.")
          Role role) {
    if (!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
      event.reply("You do not have permission to manage roles.").queue();
      return;
    }
    AllowedSelfRole allowedSelfRole =
        new AllowedSelfRole(role.getIdLong(), role.getName(), event.getGuild().getIdLong());
    repository.save(allowedSelfRole);
    event.reply("Members with %s role can now create their own roles.".formatted(role)).queue();
  }
}
