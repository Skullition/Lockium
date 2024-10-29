package dev.skullition.lockium.service.command.growtopia;

import dev.skullition.lockium.client.GrowtopiaDetailClient;
import dev.skullition.lockium.service.supplier.embed.EmbedStarterSupplier;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GlobalSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Value;

/** Command to show Growtopia related stats, such as current online users and current WOTD. */
@Command
public class SlashStats extends ApplicationCommand {
  private final GrowtopiaDetailClient client;
  private final EmbedStarterSupplier embedStarterSupplier;

  @Value("${growtopia.render-url}")
  private String renderUrl;

  public SlashStats(GrowtopiaDetailClient client, EmbedStarterSupplier embedStarterSupplier) {
    this.client = client;
    this.embedStarterSupplier = embedStarterSupplier;
  }

  /**
   * Handles the {@code /slash stats} command.
   *
   * @param event the {@link GlobalSlashEvent} that triggered this.
   */
  @JDASlashCommand(
      name = "growtopia",
      subcommand = "stats",
      description = "Gets data about the game's server stats.")
  public void onSlashStats(GlobalSlashEvent event) {
    var detail = client.getGrowtopiaDetail();
    if (detail == null) {
      event
          .reply("It seems that I can't fetch this right now. Perhaps try again later? [500]")
          .queue();
      return;
    }
    String wotd = detail.wotd().fullSize().substring(7);
    int dotIndex = wotd.indexOf(".");

    MessageEmbed embed =
        embedStarterSupplier
            .get(event)
            .addField("Online Users:", detail.onlineUsers(), false)
            .addField("WOTD:", wotd.substring(0, dotIndex).toUpperCase(), false)
            .setImage(renderUrl + wotd.toLowerCase())
            .build();
    event.replyEmbeds(embed).queue();
  }
}
