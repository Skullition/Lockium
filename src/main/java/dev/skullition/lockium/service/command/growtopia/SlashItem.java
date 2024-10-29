package dev.skullition.lockium.service.command.growtopia;

import dev.skullition.lockium.client.GrowtopiaWikiClient;
import dev.skullition.lockium.model.GrowtopiaItem;
import dev.skullition.lockium.model.GrowtopiaItemAutocompleteCache;
import dev.skullition.lockium.service.supplier.autocomplete.GrowtopiaItemAutocompleteSupplier;
import dev.skullition.lockium.service.supplier.embed.EmbedStarterSupplier;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import io.github.freya022.botcommands.api.commands.application.slash.autocomplete.annotations.AutocompleteHandler;
import java.util.Collection;
import java.util.Optional;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Command to get Growtopia item data information. */
@Command
public class SlashItem extends ApplicationCommand {
  private static final String ITEM_AUTOCOMPLETE_NAME = "SlashItem: item";
  private final GrowtopiaWikiClient proxy;
  private final EmbedStarterSupplier embedStarterSupplier;
  private final GrowtopiaItemAutocompleteSupplier itemAutocompleteSupplier;

  /**
   * Command to handle obtaining item data information.
   *
   * @param proxy wiki proxy, to get data from the Growtopia wiki.
   * @param embedStarterSupplier the embed starter, used to prevent duplicate code.
   * @param itemAutocompleteSupplier the item autocomplete supplier, used to get the list of items
   *     to autocomplete from.
   */
  public SlashItem(
      final GrowtopiaWikiClient proxy,
      EmbedStarterSupplier embedStarterSupplier,
      GrowtopiaItemAutocompleteSupplier itemAutocompleteSupplier) {
    this.proxy = proxy;
    this.embedStarterSupplier = embedStarterSupplier;
    this.itemAutocompleteSupplier = itemAutocompleteSupplier;
  }

  /**
   * Handles the {@code /growtopia item} slash command. Retrieves item data either from the
   * Growtopia Wiki or internally based on the user's choice.
   *
   * @param event the {@link GuildSlashEvent} representing the slash command interaction
   * @param itemAutocomplete the name of the item to retrieve data for
   * @param shouldGetDataFromWiki whether the data should be fetched from the wiki or internally
   */
  @TopLevelSlashCommandData(description = "Slash commands related to Growtopia.")
  @JDASlashCommand(
      name = "growtopia",
      subcommand = "item",
      description = "Growtopia item data information.")
  public void onSlashItem(
      GuildSlashEvent event,
      @NotNull
          @SlashOption(
              name = "item_name",
              description = "The item name you are looking for.",
              autocomplete = ITEM_AUTOCOMPLETE_NAME)
          GrowtopiaItemAutocompleteCache itemAutocomplete,
      @Nullable
          @SlashOption(
              name = "get_data_from_wiki",
              description = "Whether the data should be fetched from the wiki or internally.")
          Boolean shouldGetDataFromWiki) {
    if (shouldGetDataFromWiki != null && shouldGetDataFromWiki) {
      getDataFromWiki(event, itemAutocomplete.name());
      return;
    }
    // TODO: Get data internally
    event.reply(itemAutocomplete.toString()).queue();
  }

  /**
   * Handles item autocomplete events.
   *
   * @param event the command autocomplete event.
   * @return a {@link Collection} of item names.
   */
  @AutocompleteHandler(ITEM_AUTOCOMPLETE_NAME)
  public Collection<String> onItemAutocomplete(CommandAutoCompleteInteractionEvent event) {
    return itemAutocompleteSupplier.getList().stream()
        .map(GrowtopiaItemAutocompleteCache::name)
        .toList();
  }

  private void getDataFromWiki(GuildSlashEvent event, @NotNull String itemName) {
    Optional<GrowtopiaItem> result = proxy.getItemData(itemName);
    if (result.isEmpty()) {
      event.reply("No item found with name `" + itemName + "`. [404]").queue();
      return;
    }
    GrowtopiaItem item = result.get();
    MessageEmbed embed =
        embedStarterSupplier
            .get(event)
            .setAuthor(itemName, item.itemWikiUrl())
            .setTitle(item.itemWikiUrl())
            .setThumbnail(item.spriteUrl())
            .setDescription(item.description())
            .addField("🔧 Properties: ", item.properties(), false)
            .addField("✨ Rarity:", String.valueOf(item.rarity()), false)
            .build();

    event.replyEmbeds(embed).queue();
  }
}
