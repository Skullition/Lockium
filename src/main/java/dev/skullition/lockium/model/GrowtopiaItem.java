package dev.skullition.lockium.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.skullition.lockium.deserializer.ItemPropertyDeserializer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public record GrowtopiaItem(
    int id,
    String description,
    @JsonDeserialize(using = ItemPropertyDeserializer.class) EnumSet<ItemProperty> properties) {
  public enum ItemProperty {
    MULTI_FACING(0x01),
    WRENCH_ABLE(0x02),
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

    public static EnumSet<ItemProperty> fromInt(int flags) {
      return Arrays.stream(values())
          .filter(property -> (flags & property.value) != 0)
          .collect(Collectors.toCollection(() -> EnumSet.noneOf(ItemProperty.class)));
    }
  }
}
