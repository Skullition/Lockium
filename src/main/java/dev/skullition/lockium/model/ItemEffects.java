package dev.skullition.lockium.model;

import org.jetbrains.annotations.NotNull;

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
