package dev.skullition.lockium.util;

import dev.skullition.lockium.service.supplier.ApplicationEmojiSupplier;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/** Utils class for {@link dev.skullition.lockium.model.GrowtopiaItem}. */
public class ItemUtils {
  /**
   * Converts a string chi name to the corresponding {@link Emoji} instance.
   *
   * @param chi the string chi name.
   * @param emojiSupplier the emoji supplier to convert from.
   * @return the emoji.
   */
  public static Emoji stringChiToEmoji(String chi, ApplicationEmojiSupplier emojiSupplier) {
    return switch (chi) {
      case "Earth" -> emojiSupplier.getEmojiByName("earth");
      case "Fire" -> emojiSupplier.getEmojiByName("fire");
      case "Wind", "Air" -> emojiSupplier.getEmojiByName("wind");
      case "Water" -> emojiSupplier.getEmojiByName("water");
      default -> emojiSupplier.getEmojiByName("unknown");
    };
  }

  @NotNull
  @Contract(pure = true)
  public static String getWikiItemName(@NotNull String itemName) {
    return itemName.replaceAll(" ", "_");
  }
}
