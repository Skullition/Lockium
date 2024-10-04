package dev.skullition.lockium.service.command.growtopia;

import dev.skullition.lockium.model.GrowtopiaItem;
import dev.skullition.lockium.proxy.GrowtopiaWikiProxy;
import dev.skullition.lockium.service.supplier.embed.EmbedStarterSupplier;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import java.util.Optional;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Command to get Growtopia item data information. */
@Command
public class SlashItem extends ApplicationCommand {
  private final GrowtopiaWikiProxy proxy;
  private final EmbedStarterSupplier embedStarterSupplier;

  public SlashItem(final GrowtopiaWikiProxy proxy, EmbedStarterSupplier embedStarterSupplier) {
    this.proxy = proxy;
    this.embedStarterSupplier = embedStarterSupplier;
  }

  /**
   * Handles the {@code /growtopia item} slash command. Retrieves item data either from the
   * Growtopia Wiki or internally based on the user's choice.
   *
   * @param event the {@link GuildSlashEvent} representing the slash command interaction
   * @param itemName the name of the item to retrieve data for
   * @param shouldGetDataFromWiki whether the data should be fetched from the wiki or internally
   */
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
      return;
    }
    // TODO: Get data internally
    event.reply("TODO: Get data internally.").queue();
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
