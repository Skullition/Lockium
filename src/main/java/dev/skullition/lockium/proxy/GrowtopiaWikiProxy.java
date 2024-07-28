package dev.skullition.lockium.proxy;

import org.jetbrains.annotations.NotNull;
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

    public Optional<String> getItemData(@NotNull String itemName) {
        String resolvedItemName = getWikiItemName(itemName);
        Document document;
        try {
            document = Jsoup.connect(wikiUrl + resolvedItemName).get();
        } catch (IOException e) {
            return Optional.empty();
        }
        Element spriteElement = document.select(".item-card .card-header img").first();
        if (spriteElement == null) {
            return Optional.empty();
        }
        String spriteUrl = spriteElement.attr("src");
    }

    @NotNull
    private String getWikiItemName(@NotNull String itemName) {
        return itemName.replaceAll(" ", "_");
    }
}
