package dev.skullition.lockium.service.command.growtopia;

import dev.skullition.lockium.model.GrowtopiaItem;
import dev.skullition.lockium.proxy.GrowtopiaWikiProxy;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Command
public class SlashItem extends ApplicationCommand {
    private static final Logger log = LoggerFactory.getLogger(SlashItem.class);
    private final GrowtopiaWikiProxy proxy;

    public SlashItem(final GrowtopiaWikiProxy proxy) {
        this.proxy = proxy;
    }

    //TODO: pull item data from database, not directly from wiki
    @TopLevelSlashCommandData(description = "Slash commands related to Growtopia.")
    @JDASlashCommand(name = "growtopia", subcommand = "item", description = "Growtopia item data information.")
    public void onSlashItem(GuildSlashEvent event,
                            @NotNull @SlashOption(name = "item_name", description = "The item name you are looking for.") String itemName,
                            @SlashOption(name = "get_data_from_wiki", description = "Whether the data should be fetched from the wiki or internally.") boolean getDataFromWiki) {
        if (getDataFromWiki) {
            Optional<GrowtopiaItem> result = proxy.getItemData(itemName);
            if (result.isEmpty()) {
                event.reply("No item found with name `" + itemName + "`. [404]").queue();
                return;
            }
            GrowtopiaItem item = result.get();
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor(itemName, item.itemWikiUrl())
                    .setTitle(item.itemWikiUrl())
                    .setThumbnail(item.spriteUrl())
                    .setDescription(item.description())
                    .addField("\uD83D\uDD27 Properties: ", item.properties(), false)
                    .addField("✨ Rarity:", String.valueOf(item.rarity()), false);

            event.replyEmbeds(embedBuilder.build()).queue();
        }
    }
}
