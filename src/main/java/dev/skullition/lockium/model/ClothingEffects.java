package dev.skullition.lockium.model;

import org.jetbrains.annotations.NotNull;

/**
 * Record to store effects when a player wears a clothing item (null if item does not give effects).
 *
 * @param onWearingText text shown when a player wears a clothing item with an effect.
 * @param onRemoveText text shown when a player removes a clothing item with an effect.
 * @param effect player mod obtained when a player uses a clothing item.
 */
public record ClothingEffects(
    @NotNull String onWearingText, @NotNull String onRemoveText, @NotNull String effect) {}
