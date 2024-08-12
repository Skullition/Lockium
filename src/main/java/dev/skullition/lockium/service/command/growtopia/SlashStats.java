package dev.skullition.lockium.service.command.growtopia;

import dev.skullition.lockium.model.GrowtopiaDetail;
import dev.skullition.lockium.proxy.GrowtopiaDetailProxy;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

@Command
public class SlashStats extends ApplicationCommand {
  private final GrowtopiaDetailProxy proxy;

  public SlashStats(GrowtopiaDetailProxy proxy) {
    this.proxy = proxy;
  }

  @JDASlashCommand(
      name = "growtopia",
      subcommand = "stats",
      description = "Gets data about the game's server stats.")
  public void onSlashStats(GuildSlashEvent event) {
    var proxyResult = proxy.getGrowtopiaDetail();
    if (proxyResult.isEmpty()) {
      event
          .reply("It seems that I can't fetch this right now. Perhaps try again later? [500]")
          .queue();
      return;
    }

    GrowtopiaDetail growtopiaDetail = proxyResult.get();
    MessageEmbed embed =
        new EmbedBuilder()
            .addField("Online Users:", growtopiaDetail.onlineUsers(), false)
            .addField("WOTD:", growtopiaDetail.wotdName(), false)
            .build();
    event.replyEmbeds(embed).queue();
  }
}
