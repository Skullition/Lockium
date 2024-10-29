package dev.skullition.lockium.service.supplier.autocomplete;

import dev.skullition.lockium.model.GrowtopiaItemAutocompleteCache;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/** interface for supplying autocomplete options of item names. */
public interface GrowtopiaItemAutocompleteSupplier {
  @NotNull
  List<GrowtopiaItemAutocompleteCache> getList();

  @NotNull
  Map<Integer, GrowtopiaItemAutocompleteCache> getMap();
}
