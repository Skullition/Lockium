package dev.skullition.lockium.service.supplier.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

/**
 * Interface for supplying a pre-configured {@link EmbedBuilder} based on a 
 * {@link GenericInteractionCreateEvent}. This is useful for creating standardized 
 * {@code EmbedBuilder} instances without duplicating common configuration logic.
 *
 * <p>Typical usage might involve setting default fields such as color, author, or 
 * footer, so that each embed starts from a consistent state.</p>
 *
 * <p>For example:</p>
 * {@snippet :
 * public class MyEmbedStarter implements EmbedStarterSupplier {
 *     
 *     public EmbedBuilder get(GenericInteractionCreateEvent event) {
 *         EmbedBuilder builder = new EmbedBuilder();
 *         builder.setColor(Color.BLUE)
 *                .setFooter("Requested by " + user.getName(), user.getAvatarUrl());
 *         return builder;
 *     }
 * }
 * }
 */
public interface EmbedStarterSupplier {
  EmbedBuilder get(GenericInteractionCreateEvent event);
}
