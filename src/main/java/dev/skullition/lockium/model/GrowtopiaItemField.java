package dev.skullition.lockium.model;

import org.jetbrains.annotations.NotNull;

public record GrowtopiaItemField(
    @NotNull String type,
    @NotNull String chi,
    @NotNull String textureType,
    @NotNull String collisionType,
    @NotNull String hitsToBreak,
    @NotNull String seedColor,
    @NotNull String growTime,
    @NotNull String gems) {}
