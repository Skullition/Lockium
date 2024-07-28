package dev.skullition.lockium.proxy;

import dev.skullition.lockium.model.GrowtopiaItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class GrowtopiaWikiProxy {
    @Value("${growtopia.wiki-url}")
    private String wikiUrl;
    private static final int MAX_RARITY = 999;

    public Optional<GrowtopiaItem> getItemData(@NotNull String itemName) {
        String resolvedItemName = getWikiItemName(itemName);
        String itemWikiUrl = wikiUrl + resolvedItemName;
        Document document;
        try {
            document = Jsoup.connect(itemWikiUrl).get();
        } catch (IOException e) {
            return Optional.empty();
        }
        Element spriteElement = document.selectFirst(".item-card .card-header img");
        @Nullable String spriteUrl = spriteElement == null ? "This item does not have a link." : spriteElement.attr("src");

        Element rarityElement = document.selectFirst("div.card-header small");
        int rarity = rarityElement == null ? MAX_RARITY : Integer.parseInt(rarityElement.text().replaceAll("(Rarity: )|\\D+", ""));

        Element descriptionElement = document.selectFirst("div.card-text");
        String description = descriptionElement == null ? "*This item has no description.*" : descriptionElement.text();
        return Optional.of(new GrowtopiaItem(spriteUrl, itemWikiUrl, rarity, description));
    }

    @NotNull
    private String getWikiItemName(@NotNull String itemName) {
        return itemName.replaceAll(" ", "_");
    }
}
