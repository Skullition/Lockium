package dev.skullition.lockium.service.supplier.embed;

import java.time.Instant;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.springframework.stereotype.Service;

/**
 * A default implementation of the {@link EmbedStarterSupplier} interface, providing a standardized
 * {@link EmbedBuilder} with a preset color, footer, and timestamp.
 */
@Service
public class DefaultEmbedStarterSupplier implements EmbedStarterSupplier {
  private static final int MAGENTA_ELEPHANT = 14025328;

  @Override
  public EmbedBuilder get(GenericInteractionCreateEvent event) {
    User user = event.getUser();
    return new EmbedBuilder()
        .setColor(MAGENTA_ELEPHANT)
        .setFooter("Requested by " + user.getName(), user.getAvatarUrl())
        .setTimestamp(Instant.now());
  }
}
