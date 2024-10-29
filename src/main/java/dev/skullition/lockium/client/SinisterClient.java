package dev.skullition.lockium.client;

import dev.skullition.lockium.model.GrowtopiaItem;
import dev.skullition.lockium.model.GrowtopiaItemAutocompleteCache;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * instance of {@link org.springframework.web.client.RestClient} to call Sinister APIs internally.
 */
@HttpExchange(url = "/api/GrowtopiaItems")
public interface SinisterClient {
  @GetExchange
  List<GrowtopiaItemAutocompleteCache> getGrowtopiaItemsCache();

  @GetExchange("/{id}")
  GrowtopiaItem getGrowtopiaItemById(@PathVariable int id);
}
