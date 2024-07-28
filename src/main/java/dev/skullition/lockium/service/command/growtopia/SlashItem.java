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
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Command
public class SlashItem extends ApplicationCommand {
    private final GrowtopiaWikiProxy proxy;

    public SlashItem(final GrowtopiaWikiProxy proxy) {
        this.proxy = proxy;
    }

    //TODO: pull item data from database, not directly from wiki
    @TopLevelSlashCommandData(description = "Slash commands related to Growtopia.")
    @JDASlashCommand(name = "growtopia", subcommand = "item", description = "Growtopia item data information.")
    public void onSlashItem(GuildSlashEvent event,
                            @NotNull @SlashOption(name = "item_name", description = "Item name you are looking for.") String itemName) {
        Optional<GrowtopiaItem> result = proxy.getItemData(itemName);
        if (result.isEmpty()) {
            event.reply("No item found with name `" + itemName + "`. [404]").queue();
            return;
        }
        GrowtopiaItem item = result.get();
        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(itemName, item.itemWikiUrl())
                .setTitle(item.itemWikiUrl())
                .setThumbnail(item.spriteUrl())
                .setDescription(item.description())
                .addField("Rarity", String.valueOf(item.rarity()), false)
                .build();

        event.replyEmbeds(embed).queue();
    }
}
