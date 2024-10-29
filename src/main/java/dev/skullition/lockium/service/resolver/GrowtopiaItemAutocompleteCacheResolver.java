package dev.skullition.lockium.service.resolver;

import dev.skullition.lockium.model.GrowtopiaItemAutocompleteCache;
import dev.skullition.lockium.service.supplier.autocomplete.GrowtopiaItemAutocompleteSupplier;
import io.github.freya022.botcommands.api.commands.application.slash.SlashCommandInfo;
import io.github.freya022.botcommands.api.core.BotOwners;
import io.github.freya022.botcommands.api.core.service.annotations.Resolver;
import io.github.freya022.botcommands.api.parameters.ClassParameterResolver;
import io.github.freya022.botcommands.api.parameters.resolvers.SlashParameterResolver;
import java.util.Collection;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Resolves item name to an instance of {@link GrowtopiaItemAutocompleteCache}. */
@Resolver
public class GrowtopiaItemAutocompleteCacheResolver
    extends ClassParameterResolver<
        GrowtopiaItemAutocompleteCacheResolver, GrowtopiaItemAutocompleteCache>
    implements SlashParameterResolver<
        GrowtopiaItemAutocompleteCacheResolver, GrowtopiaItemAutocompleteCache> {
  private final GrowtopiaItemAutocompleteSupplier itemSupplier;
  private final BotOwners owners;

  /**
   * Creates an instance of this resolver to resolve item names to {@link
   * GrowtopiaItemAutocompleteCache}.
   */
  public GrowtopiaItemAutocompleteCacheResolver(
      GrowtopiaItemAutocompleteSupplier itemSupplier, BotOwners owners) {
    super(GrowtopiaItemAutocompleteCache.class);
    this.itemSupplier = itemSupplier;
    this.owners = owners;
  }

  @NotNull
  @Override
  public OptionType getOptionType() {
    return OptionType.STRING;
  }

  @NotNull
  @Override
  public Collection<Command.Choice> getPredefinedChoices(@Nullable Guild guild) {
    List<GrowtopiaItemAutocompleteCache> itemList = itemSupplier.getList();
    // Predefined choices of Dirt, World Lock, and Laser Grid
    var predefinedItemsList = List.of(itemList.get(2), itemList.get(242), itemList.get(5666));
    return predefinedItemsList.stream()
        .map(item -> new Command.Choice(item.name(), item.id()))
        .toList();
  }

  @Nullable
  @Override
  public GrowtopiaItemAutocompleteCache resolve(
      @NotNull SlashCommandInfo info,
      @NotNull CommandInteractionPayload event,
      @NotNull OptionMapping optionMapping) {
    if (owners.isOwner(event.getUser())) {
      return resolveOwnerItemLookupById(optionMapping);
    }
    return resolveItemLookupByName(optionMapping.getAsString());
  }

  @Nullable
  private GrowtopiaItemAutocompleteCache resolveOwnerItemLookupById(OptionMapping optionMapping) {
    try {
      int itemId = optionMapping.getAsInt();
      return itemSupplier.getMap().get(itemId);
    } catch (NumberFormatException e) {
      return resolveItemLookupByName(optionMapping.getAsString());
    }
  }

  @Nullable
  private GrowtopiaItemAutocompleteCache resolveItemLookupByName(String itemName) {
    // Maybe return list of items that matched instead
    return itemSupplier.getList().stream()
        .filter(item -> item.name().toLowerCase().contains(itemName.toLowerCase()))
        .findFirst()
        .orElse(null);
  }
}
