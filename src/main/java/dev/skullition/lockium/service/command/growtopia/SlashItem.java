package dev.skullition.lockium.service.command.growtopia;

import dev.skullition.lockium.model.GrowtopiaItem;
import dev.skullition.lockium.proxy.GrowtopiaWikiProxy;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.ObjectProvider;

@Command
public class SlashItem extends ApplicationCommand {
  private final GrowtopiaWikiProxy proxy;
  private final ObjectProvider<EmbedBuilder> embedBuilderObjectProvider;

  public SlashItem(
      final GrowtopiaWikiProxy proxy, ObjectProvider<EmbedBuilder> embedBuilderObjectProvider) {
    this.proxy = proxy;
    this.embedBuilderObjectProvider = embedBuilderObjectProvider;
  }

  @TopLevelSlashCommandData(description = "Slash commands related to Growtopia.")
  @JDASlashCommand(
      name = "growtopia",
      subcommand = "item",
      description = "Growtopia item data information.")
  public void onSlashItem(
      GuildSlashEvent event,
      @NotNull @SlashOption(name = "item_name", description = "The item name you are looking for.")
          String itemName,
      @Nullable
          @SlashOption(
              name = "get_data_from_wiki",
              description = "Whether the data should be fetched from the wiki or internally.")
          Boolean shouldGetDataFromWiki) {
    if (shouldGetDataFromWiki != null && shouldGetDataFromWiki) {
      getDataFromWiki(event, itemName);
    } else {
      // TODO: Get data internally
    }
  }

  private void getDataFromWiki(GuildSlashEvent event, @NotNull String itemName) {
    Optional<GrowtopiaItem> result = proxy.getItemData(itemName);
    if (result.isEmpty()) {
      event.reply("No item found with name `" + itemName + "`. [404]").queue();
      return;
    }
    GrowtopiaItem item = result.get();
    MessageEmbed embed =
        embedBuilderObjectProvider
            .getObject(event)
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
