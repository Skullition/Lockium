package dev.skullition.lockium.service.command.selfrole;

import io.github.freya022.botcommands.api.commands.annotations.BotPermissions;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.annotations.UserPermissions;
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

/**
* Command to set up which roles are allowed to create and update self roles.
*/
@Command
@UserPermissions({Permission.MANAGE_ROLES, Permission.NICKNAME_MANAGE})
@BotPermissions(Permission.MANAGE_ROLES)
public class SlashSetup extends ApplicationCommand {
  @TopLevelSlashCommandData(
      scope = CommandScope.GUILD,
      contexts = InteractionContextType.GUILD,
      integrationTypes = IntegrationType.GUILD_INSTALL,
      defaultLocked = true,
      description = "Commands related to creating and updating self roles.")
  @JDASlashCommand(
      name = "self_role",
      subcommand = "setup",
      description = "Sets up self roles for this guild.")
  public void onSlashSetup(
      GuildSlashEvent event,
      @NotNull @SlashOption(name = "role", description = "Which role to allow creating self roles.")
          Role role) {
    // TODO: Store allowed roles in database.
    event.reply("test").queue();
  }
}
