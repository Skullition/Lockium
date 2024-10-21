package dev.skullition.lockium.proxy;

import dev.skullition.lockium.model.GrowtopiaItemAutocompleteCache;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
* Proxy to call internal Sinister APIs.
*/
@Service
public class SinisterProxy {
  private final WebClient webClient;

  public SinisterProxy(WebClient webClient) {
    this.webClient = webClient;
  }

  @NotNull
  public Flux<GrowtopiaItemAutocompleteCache> getItemCache() {
    return webClient.get().retrieve().bodyToFlux(GrowtopiaItemAutocompleteCache.class);
  }
}
