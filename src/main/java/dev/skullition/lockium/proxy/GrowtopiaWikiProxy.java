package dev.skullition.lockium.proxy;

import dev.skullition.lockium.model.GrowtopiaItem;
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

import java.io.IOException;
import java.util.Optional;

@Service
public class GrowtopiaWikiProxy {
    private static final int MAX_RARITY = 999;
    private static final Logger logger = LoggerFactory.getLogger(GrowtopiaWikiProxy.class);
    @Value("${growtopia.wiki-url}")
    private String wikiUrl;

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
        @Nullable String spriteUrl = spriteElement == null ? "https://growtopia.fandom.com" : spriteElement.attr("src");

        Element rarityElement = document.selectFirst("div.card-header small");
        int rarity = rarityElement == null ? MAX_RARITY : Integer.parseInt(rarityElement.text().replaceAll("(Rarity: )|\\D+", ""));

        Elements cardTextElements = document.select("div.card-text");

        Element descriptionElement = cardTextElements.first();
        String description = descriptionElement == null ? "*This item has no description.*" : descriptionElement.text();

        String properties = cardTextElements.get(1).wholeText();

        return Optional.of(new GrowtopiaItem(spriteUrl, itemWikiUrl, rarity, description, properties));
    }

    @NotNull
    private String getWikiItemName(@NotNull String itemName) {
        return itemName.replaceAll(" ", "_");
    }
}
