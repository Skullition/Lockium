package dev.skullition.lockium.service.supplier.autocomplete;

import dev.skullition.lockium.model.GrowtopiaItemAutocompleteCache;
import java.util.Collection;

/**
* interface for supplying autocomplete options of item names.
*/
public interface GrowtopiaItemAutocompleteSupplier {
  Collection<GrowtopiaItemAutocompleteCache> get();
}
