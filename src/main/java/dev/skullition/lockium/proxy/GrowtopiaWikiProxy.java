package dev.skullition.lockium.proxy;

import dev.skullition.lockium.builder.GrowtopiaItemFieldBuilder;
import dev.skullition.lockium.model.ClothingEffects;
import dev.skullition.lockium.model.GrowtopiaItem;
import dev.skullition.lockium.model.GrowtopiaItemField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Proxy to get data from the Growtopia Wiki. */
@Service
public class GrowtopiaWikiProxy {
  private static final int MAX_RARITY = 999;
  private static final Logger logger = LoggerFactory.getLogger(GrowtopiaWikiProxy.class);

  @Value("${growtopia.wiki-url}")
  private String wikiUrl;

  /**
   * Get data about an item based from a Case-Sensitive item name.
   *
   * @param itemName the item name.
   * @return a {@link Optional} that could contain the queried item data, returns empty optional if
   *     item is not found.
   */
  public Optional<GrowtopiaItem> getItemData(@NotNull String itemName) {
    logger.debug("Fetching item data for {}", itemName);
    String resolvedItemName = getWikiItemName(itemName);
    String itemWikiUrl = wikiUrl + resolvedItemName;
    Document document;
    try {
      document = Jsoup.connect(itemWikiUrl).get();
    } catch (IOException e) {
      return Optional.empty();
    }
    Element spriteElement = document.selectFirst(".item-card .card-header img");
    @Nullable
    final String spriteUrl =
        spriteElement == null ? "https://growtopia.fandom.com" : spriteElement.attr("src");

    Element rarityElement = document.selectFirst("div.card-header small");
    final int rarity =
        rarityElement == null
            ? MAX_RARITY
            : Integer.parseInt(rarityElement.text().replaceAll("(Rarity: )|\\D+", ""));

    Elements cardTextElements = document.select("div.card-text");

    Element descriptionElement = cardTextElements.first();
    final String description =
        descriptionElement == null ? "*This item has no description.*" : descriptionElement.text();

    final String properties = cardTextElements.get(1).wholeText();

    Element cardFieldElement = document.selectFirst("table.card-field");
    // this should never trigger
    if (cardFieldElement == null) {
      logger.error("No card field found in {}", itemName);
      return Optional.empty();
    }
    Elements tableRows = cardFieldElement.getElementsByTag("tr");

    var builder = new GrowtopiaItemFieldBuilder();
    for (Element row : tableRows) {
      Element cellHeader = row.selectFirst("th");
      Element cellBody = row.selectFirst("td");
      // this should also never trigger
      if (cellHeader == null || cellBody == null) {
        logger.error("Cell header is empty in {}, for row {}", itemName, row);
        return Optional.empty();
      }

      switch (cellHeader.text()) {
        case "Type" -> builder.setType(cellBody.text());
        case "Chi" -> builder.setChi(cellBody.text());
        case "Texture Type" -> builder.setTextureType(cellBody.text());
        case "Collision Type" -> builder.setCollisionType(cellBody.text());
        case "Hardness" -> builder.setHitsToBreak(resolveHitsToBreak(cellBody.text()));
        case "Seed Color" -> builder.setSeedColor(cellBody.text());
        case "Grow Time" -> builder.setGrowTime(cellBody.text());
        case "Default Gems Drop" -> builder.setGems(cellBody.text());
        default -> logger.error("Unknown cell header {}", cellHeader);
      }
    }
    GrowtopiaItemField itemField = builder.build();
    logger.debug("Item field for {} is {}", itemName, itemField);

    Optional<String> optionalOnWearing =
        getParentElementText(document.selectFirst("img[alt=\"CheckboxEnabled\"]"));
    Optional<String> optionalOnRemoving =
        getParentElementText(document.selectFirst("img[alt=\"Checkbox0\"]"));
    Optional<String> optionalEffect =
        getParentElementChildItemModText(document.selectFirst("a[href=\"/wiki/Mods\"]"));

    ClothingEffects clothingEffects;
    if (optionalOnWearing.isEmpty() || optionalOnRemoving.isEmpty() || optionalEffect.isEmpty()) {
      clothingEffects = null;
    } else {
      String onWearingText = optionalOnWearing.get();
      String onRemovingText = optionalOnRemoving.get();
      String itemMod = optionalEffect.get();
      clothingEffects = new ClothingEffects(onWearingText, onRemovingText, itemMod);
    }
    logger.debug("Item effect for {} is {}", itemName, clothingEffects);

    return Optional.of(
        new GrowtopiaItem(
            spriteUrl, itemWikiUrl, rarity, description, properties, itemField, clothingEffects));
  }

  @NotNull
  private Optional<String> getParentElementText(@Nullable Element element) {
    return Optional.ofNullable(element).map(Element::parent).map(Element::text);
  }

  @NotNull
  private Optional<String> getParentElementChildItemModText(@Nullable Element element) {
    return Optional.ofNullable(element)
        .map(Element::parent)
        .map(node -> node.selectFirst("i"))
        .map(Element::text);
  }

  @NotNull
  private String getWikiItemName(@NotNull String itemName) {
    return itemName.replaceAll(" ", "_");
  }

  @NotNull
  private String resolveHitsToBreak(@NotNull String hitsToBreak) {
    List<String> hits = new ArrayList<>();
    for (String string : hitsToBreak.split(" ")) {
      if (string.matches("-?\\d+")) {
        hits.add(string);
      }
    }
    return "🤜 " + hits.get(0) + " Hits (⛏ " + hits.get(1) + " Hits)";
  }
}
