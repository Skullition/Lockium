package dev.skullition.lockium.model;

import org.jetbrains.annotations.NotNull;

public record GrowtopiaItem(
        @NotNull String spriteUrl,
        @NotNull String itemWikiUrl,
        int rarity,
        @NotNull String description
) {
}
