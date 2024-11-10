package dev.skullition.lockium.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** DTO of items from the Growtopia wiki. */
public record GrowtopiaWikiItem(
    @NotNull String spriteUrl,
    @NotNull String itemWikiUrl,
    int rarity,
    @NotNull String description,
    @NotNull String properties,
    @NotNull GrowtopiaItemField itemField,
    @Nullable ItemEffects itemEffects) {

  /** DTO of the specific properties of items. */
  public record GrowtopiaItemField(
      @NotNull String type,
      @NotNull String chi,
      @NotNull String textureType,
      @NotNull String collisionType,
      @NotNull String hitsToBreak,
      @NotNull String seedColor,
      @NotNull String growTime,
      @NotNull String gems) {}

  /**
   * Record to store effects when a player uses an item with effects (null if item does not give
   * effects).
   *
   * @param onUseText text shown when a player uses an item with an effect.
   * @param onRemoveText text shown when a player removes an item with an effect.
   * @param effect player mod obtained when a player uses an item with an effect.
   */
  public record ItemEffects(
      @NotNull String onUseText, @NotNull String onRemoveText, @NotNull String effect) {}
}
