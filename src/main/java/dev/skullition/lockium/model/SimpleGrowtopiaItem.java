package dev.skullition.lockium.model;

import org.jetbrains.annotations.NotNull;

/**
* Record for storing simple id and name pair items.
*/
public record SimpleGrowtopiaItem(int id, @NotNull String name) {}
