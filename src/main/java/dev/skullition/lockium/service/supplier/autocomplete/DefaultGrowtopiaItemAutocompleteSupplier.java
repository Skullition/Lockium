package dev.skullition.lockium.service.supplier.autocomplete;

import dev.skullition.lockium.client.SinisterClient;
import dev.skullition.lockium.model.GrowtopiaItemAutocompleteCache;
import java.util.Collection;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/** Default implementation for supplying autocomplete options of item names. */
@Service
public class DefaultGrowtopiaItemAutocompleteSupplier implements GrowtopiaItemAutocompleteSupplier {
  private final SinisterClient client;
  private Collection<GrowtopiaItemAutocompleteCache> itemAutocompleteCache;

  public DefaultGrowtopiaItemAutocompleteSupplier(SinisterClient client) {
    this.client = client;
  }

  @Override
  public Collection<GrowtopiaItemAutocompleteCache> get() {
    return itemAutocompleteCache;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    itemAutocompleteCache = client.getGrowtopiaItemsCache();
  }
}
