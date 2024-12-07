package dev.skullition.lockium.util;

import dev.skullition.lockium.model.GrowtopiaItem;
import dev.skullition.lockium.service.supplier.ApplicationEmojiSupplier;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/** Utils class for {@link GrowtopiaItem}. */
public final class ItemUtils {
  /**
   * Converts a string chi name to the corresponding {@link Emoji} instance.
   *
   * @param chi the string chi name
   * @param emojiSupplier the emoji supplier to convert from
   * @return the emoji
   */
  @NotNull
  public static Emoji stringChiToEmoji(
      @NotNull String chi, @NotNull ApplicationEmojiSupplier emojiSupplier) {
    return switch (chi) {
      case "Earth" -> emojiSupplier.getEmojiByName("earth");
      case "Fire" -> emojiSupplier.getEmojiByName("fire");
      case "Wind", "Air" -> emojiSupplier.getEmojiByName("wind");
      case "Water" -> emojiSupplier.getEmojiByName("water");
      default -> emojiSupplier.getEmojiByName("unknown");
    };
  }

  /**
   * Converts a {@link dev.skullition.lockium.model.GrowtopiaItem.GuildChest.GuildSeason} enum to
   * the corresponding {@link Emoji} instance.
   *
   * @param season the season enum
   * @param emojiSupplier the emoji supplier to convert from
   * @return the emoji
   */
  @NotNull
  public static Emoji seasonToEmoji(
      @NotNull GrowtopiaItem.GuildChest.GuildSeason season,
      @NotNull ApplicationEmojiSupplier emojiSupplier) {
    return switch (season) {
      case Summer -> emojiSupplier.getEmojiByName("summerChest");
      case Winter -> emojiSupplier.getEmojiByName("winterChest");
      case Spring -> emojiSupplier.getEmojiByName("springChest");
    };
  }

  @NotNull
  @Contract(pure = true)
  public static String getWikiItemName(@NotNull String itemName) {
    return itemName.replaceAll(" ", "_");
  }
}
