package dev.skullition.lockium.model;

import org.jetbrains.annotations.NotNull;

/**
 * Record to store values for Discord autocomplete.
 *
 * @param name the Growtopia item name
 * @param id the id corresponding to the item
 * @param isTradable whether this item can be traded among players.
 * @param isSeed whether this item is a seed.
 */
public record GrowtopiaItemAutocompleteCache(
    @NotNull String name, int id, boolean isTradable, boolean isSeed) {}
