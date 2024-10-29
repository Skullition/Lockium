package dev.skullition.lockium.service.supplier.autocomplete;

import dev.skullition.lockium.client.SinisterClient;
import dev.skullition.lockium.model.GrowtopiaItemAutocompleteCache;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/** Default implementation for supplying autocomplete options of item names. */
@Service
public class DefaultGrowtopiaItemAutocompleteSupplier implements GrowtopiaItemAutocompleteSupplier {
  private final SinisterClient client;
  private List<GrowtopiaItemAutocompleteCache> itemList;
  private Map<Integer, GrowtopiaItemAutocompleteCache> itemMap;

  public DefaultGrowtopiaItemAutocompleteSupplier(SinisterClient client) {
    this.client = client;
  }

  @NotNull
  @Override
  public List<GrowtopiaItemAutocompleteCache> getList() {
    return itemList;
  }

  @NotNull
  @Override
  public Map<Integer, GrowtopiaItemAutocompleteCache> getMap() {
    return itemMap;
  }

  /** Instantiates and caches items in data structures at app startup. */
  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    itemList = client.getGrowtopiaItemsCache();
    itemMap =
        itemList.stream()
            .collect(Collectors.toMap(GrowtopiaItemAutocompleteCache::id, item -> item));
  }
}
