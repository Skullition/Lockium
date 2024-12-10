package dev.skullition.lockium.util;

import dev.skullition.lockium.model.GrowtopiaItem;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/** Utils class for {@link GrowtopiaItem}. */
public final class ItemUtils {
  /**
   * Converts a string chi name to the corresponding {@link Emoji} instance.
   *
   * @param chi the string chi name
   * @return the emoji
   */
  @NotNull
  public static Emoji stringChiToEmoji(
      @NotNull String chi) {
    return switch (chi) {
      case "Earth" -> AppEmojis.EARTH;
      case "Fire" -> AppEmojis.FIRE;
      case "Wind", "Air" -> AppEmojis.WIND;
      case "Water" -> AppEmojis.WATER;
      default -> AppEmojis.MISSING;
    };
  }

  /**
   * Converts a {@link dev.skullition.lockium.model.GrowtopiaItem.GuildChest.GuildSeason} enum to
   * the corresponding {@link Emoji} instance.
   *
   * @param season the season enum
   * @return the emoji
   */
  @NotNull
  public static Emoji seasonToEmoji(
      @NotNull GrowtopiaItem.GuildChest.GuildSeason season) {
    return switch (season) {
      case SUMMER -> AppEmojis.SUMMER_CHEST;
      case WINTER -> AppEmojis.WINTER_CHEST;
      case SPRING -> AppEmojis.SPRING_CHEST;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static String getWikiItemName(@NotNull String itemName) {
    return itemName.replaceAll(" ", "_");
  }
}
