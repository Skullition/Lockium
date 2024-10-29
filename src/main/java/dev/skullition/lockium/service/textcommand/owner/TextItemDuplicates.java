package dev.skullition.lockium.service.textcommand.owner;

import dev.skullition.lockium.model.GrowtopiaItemAutocompleteCache;
import dev.skullition.lockium.service.supplier.autocomplete.GrowtopiaItemAutocompleteSupplier;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.text.BaseCommandEvent;
import io.github.freya022.botcommands.api.commands.text.TextCommand;
import io.github.freya022.botcommands.api.commands.text.annotations.Hidden;
import io.github.freya022.botcommands.api.commands.text.annotations.JDATextCommandVariation;
import java.util.ArrayList;
import java.util.HashSet;

/** Text command to show a list of items with duplicate names. */
@Command
public class TextItemDuplicates extends TextCommand {
  private final GrowtopiaItemAutocompleteSupplier itemAutocompleteSupplier;

  public TextItemDuplicates(GrowtopiaItemAutocompleteSupplier itemAutocompleteSupplier) {
    this.itemAutocompleteSupplier = itemAutocompleteSupplier;
  }

  /**
   * Handles and sends a list of items with duplicate names.
   *
   * @param event the event that triggered this.
   */
  @Hidden
  @JDATextCommandVariation(
      path = "itemduplicates",
      description = "Gets a list of items with duplicate names.")
  public void onTextItemDuplicates(BaseCommandEvent event) {
    var uniqueNames = new HashSet<String>();
    var duplicateItemList = new ArrayList<GrowtopiaItemAutocompleteCache>();
    for (var item : itemAutocompleteSupplier.getList()) {
      if (!uniqueNames.add(item.name())) {
        duplicateItemList.add(item);
      }
    }
    var duplicateItemsBuilder = new StringBuilder();
    for (var item : duplicateItemList) {
      duplicateItemsBuilder.append("`");
      duplicateItemsBuilder.append(item.name());
      duplicateItemsBuilder.append(":");
      duplicateItemsBuilder.append(item.id());
      duplicateItemsBuilder.append("`");
      duplicateItemsBuilder.append(",");
    }
    event.reply("Duplicate items are: " + duplicateItemsBuilder).queue();
  }
}
