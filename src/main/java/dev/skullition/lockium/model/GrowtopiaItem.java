package dev.skullition.lockium.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.skullition.lockium.deserializer.ItemPropertyDeserializer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/** DTO to store internal Sinister item response. */
public record GrowtopiaItem(
    int id,
    @NotNull String name,
    @NotNull String description,
    @JsonDeserialize(using = ItemPropertyDeserializer.class) EnumSet<ItemProperty> properties) {
  /** Enum to store item flags. */
  public enum ItemProperty {
    MULTI_FACING(0x01),
    WRENCHABLE(0x02),
    NO_SEED(0x04),
    PERMANENT(0x08),
    NO_DROP(0x10),
    NO_SELF(0x20),
    NO_SHADOW(0x40),
    WORLD_LOCK(0x80),
    BETA(0x100),
    AUTO_PICKUP(0x200),
    MOD(0x400),
    RANDOM_GROW(0x800),
    PUBLIC(0x1000),
    FOREGROUND(0x2000),
    HOLIDAY(0x4000),
    UNTRADABLE(0x8000);

    private final int value;

    ItemProperty(int flag) {
      this.value = flag;
    }

    /**
     * Convert an int flag to a {@link EnumSet} of {@link ItemProperty}.
     *
     * @param flags the int flags.
     * @return an EnumSet of ItemProperties.
     */
    public static EnumSet<ItemProperty> fromInt(int flags) {
      return Arrays.stream(values())
          .filter(property -> (flags & property.value) != 0)
          .collect(Collectors.toCollection(() -> EnumSet.noneOf(ItemProperty.class)));
    }
  }
}
