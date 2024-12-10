package dev.skullition.lockium.service.command.growtopia;

import dev.skullition.lockium.client.GrowtopiaWikiClient;
import dev.skullition.lockium.client.SinisterClient;
import dev.skullition.lockium.model.GrowtopiaItem;
import dev.skullition.lockium.model.GrowtopiaItemAutocompleteCache;
import dev.skullition.lockium.model.GrowtopiaWikiItem;
import dev.skullition.lockium.service.supplier.autocomplete.GrowtopiaItemAutocompleteSupplier;
import dev.skullition.lockium.service.supplier.embed.EmbedStarterSupplier;
import dev.skullition.lockium.util.AppEmojis;
import dev.skullition.lockium.util.ItemUtils;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GlobalSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import io.github.freya022.botcommands.api.commands.application.slash.autocomplete.annotations.AutocompleteHandler;
import java.util.Collection;
import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

/** Command to get Growtopia item data information. */
@Command
public class SlashItem extends ApplicationCommand {
  private static final String ITEM_AUTOCOMPLETE_NAME = "SlashItem: item";
  private final GrowtopiaWikiClient wikiClient;
  private final SinisterClient sinisterClient;
  private final EmbedStarterSupplier embedStarterSupplier;
  private final GrowtopiaItemAutocompleteSupplier itemAutocompleteSupplier;

  @Value("${growtopia.wiki-url}")
  private String wikiUrl;

  /**
   * Command to handle obtaining item data information.
   *
   * @param client wiki client, to get data from the Growtopia wiki.
   * @param embedStarterSupplier the embed starter, used to prevent duplicate code.
   * @param itemAutocompleteSupplier the item autocomplete supplier, used to get the list of items
   *     to autocomplete from.
   * @param sinisterClient Sinister client, to get data internally.
   */
  public SlashItem(
      final GrowtopiaWikiClient client,
      SinisterClient sinisterClient,
      EmbedStarterSupplier embedStarterSupplier,
      GrowtopiaItemAutocompleteSupplier itemAutocompleteSupplier) {
    this.wikiClient = client;
    this.sinisterClient = sinisterClient;
    this.embedStarterSupplier = embedStarterSupplier;
    this.itemAutocompleteSupplier = itemAutocompleteSupplier;
  }

  /**
   * Handles the {@code /growtopia item} slash command. Retrieves item data either from the
   * Growtopia Wiki or internally based on the user's choice.
   *
   * @param event the {@link GlobalSlashEvent} representing the slash command interaction
   * @param itemAutocomplete the name of the item to retrieve data for
   * @param shouldGetDataFromWiki whether the data should be fetched from the wiki or internally
   */
  @TopLevelSlashCommandData(
      description = "Slash commands related to Growtopia.",
      contexts = {
        InteractionContextType.GUILD,
        InteractionContextType.BOT_DM,
        InteractionContextType.PRIVATE_CHANNEL
      },
      integrationTypes = {IntegrationType.GUILD_INSTALL, IntegrationType.USER_INSTALL})
  @JDASlashCommand(
      name = "growtopia",
      subcommand = "item",
      description = "Growtopia item data information.")
  public void onSlashItem(
      GlobalSlashEvent event,
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

    EmbedBuilder embedBuilder = embedStarterSupplier.get(event);
    embedBuilder.setFooter(
        "See anything missing/incorrect? Report with /report !", event.getUser().getAvatarUrl());

    GrowtopiaItem itemData = sinisterClient.getGrowtopiaItemById(itemAutocomplete.id());

    String itemWikiUrl = wikiUrl + ItemUtils.getWikiItemName(itemData.name());
    embedBuilder.setTitle(itemWikiUrl);
    embedBuilder.setThumbnail(itemData.wikiItemSprite());
    embedBuilder.setAuthor(
        "Item info for %s".formatted(itemData.name()), itemWikiUrl, itemData.wikiSeedSprite());
    embedBuilder.setColor(itemData.baseColor());

    if (StringUtils.hasText(itemData.extraNote())) {
      embedBuilder.addField("Extra Item Notes", itemData.extraNote(), false);
    }
    String releaseDateInfo =
        StringUtils.hasText(itemData.releaseDateInfo())
            ? "%s *Item released %s*"
                .formatted(AppEmojis.TICKING_CLOCK, itemData.releaseDateInfo())
            : "";
    if (itemData.type() == GrowtopiaItem.ItemType.SEED) {
      embedBuilder.setDescription(
          " *Plant this seed to grow a %s Tree.*"
              .formatted(itemAutocompleteSupplier.getMap().get(itemData.id() - 1).name()));
    } else {
      String description =
          StringUtils.hasText(itemData.description())
              ? itemData.description()
              : "*Description missing, report to bot owners if you have it!*";
      embedBuilder.setDescription(releaseDateInfo + "\n " + description);
      embedBuilder.setDescription(
          """
          %s

          %s
          """
              .formatted(releaseDateInfo, description));
    }

    if (itemData.itemEffect() != null) {
      String missingEffectText = "(Missing! Report if you have it!)";
      String onAdd =
          StringUtils.hasText(itemData.itemEffect().onUseMessage())
              ? itemData.itemEffect().onUseMessage()
              : missingEffectText;
      String onRemove =
          StringUtils.hasText(itemData.itemEffect().onRemoveMessage())
              ? itemData.itemEffect().onRemoveMessage()
              : missingEffectText;
      embedBuilder.addField(
          "%s Item Effect (%s)"
              .formatted(AppEmojis.FAIRY_WINGS, itemData.itemEffect().name()),
          """
          %s %s
          %s %s
          """
              .formatted(AppEmojis.CHECKBOX_ENABLED, onAdd, AppEmojis.CHECKBOX_DISABLED, onRemove),
          false);
    }

    if (itemData.pbAbility() != null) {
      var pbAbility = itemData.pbAbility();
      embedBuilder.addField(
          "%s Pet Battles".formatted(AppEmojis.BATTLE_LEASH),
          "%s %s"
              .formatted(
                  ItemUtils.stringChiToEmoji(pbAbility.elementType()),
                  pbAbility.asFormattedString()),
          false);
    }

    if (itemData.storeItem() != null) {
      if (itemData.storeItem().currency() == GrowtopiaItem.StoreItem.CurrencyType.GEMS) {
        embedBuilder.addField(
            "%s From the Store".formatted(AppEmojis.GEMS),
            itemData.storeItem().asFormattedString(),
            false);
      } else if (itemData.storeItem().currency()
          == GrowtopiaItem.StoreItem.CurrencyType.GROWTOKENS) {
        embedBuilder.addField(
            "%s From the Store".formatted(AppEmojis.GROWTOKEN),
            itemData.storeItem().asFormattedString(),
            false);
      }
      // CurrencyType.MONEY is unimplemented
    }

    if (itemData.lockeItem() != null) {
      embedBuilder.addField(
          "%s From Locke".formatted(AppEmojis.LOCKE),
          itemData.lockeItem().asFormattedString(),
          false);
    }

    if (itemData.guildChestReward() != null) {
      embedBuilder.addField(
          "%s Seasonal Guild Chest Reward"
              .formatted(
                  ItemUtils.seasonToEmoji(itemData.guildChestReward().season())),
          itemData.guildChestReward().asFormattedString(),
          false);
    }

    if (itemData.dailyChallengeReward() != null) {
      embedBuilder.addField(
          "%s Daily Challenge Reward".formatted(AppEmojis.CHALLENGE_BOARD),
          itemData.dailyChallengeReward().asFormattedString(),
          false);
    }

    event.replyEmbeds(embedBuilder.build()).queue();
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
        .filter(item -> !item.isSeed())
        .map(GrowtopiaItemAutocompleteCache::name)
        .toList();
  }

  private void getDataFromWiki(GlobalSlashEvent event, @NotNull String itemName) {
    Optional<GrowtopiaWikiItem> result = wikiClient.getItemData(itemName);
    if (result.isEmpty()) {
      event.reply("No item found with name `" + itemName + "`. [404]").queue();
      return;
    }
    GrowtopiaWikiItem item = result.get();
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
