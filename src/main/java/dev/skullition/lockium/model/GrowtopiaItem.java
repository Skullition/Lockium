package dev.skullition.lockium.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record GrowtopiaItem(
    @NotNull String spriteUrl,
    @NotNull String itemWikiUrl,
    int rarity,
    @NotNull String description,
    @NotNull String properties,
    @NotNull GrowtopiaItemField itemField,
    @Nullable ItemEffects itemEffects) {}
