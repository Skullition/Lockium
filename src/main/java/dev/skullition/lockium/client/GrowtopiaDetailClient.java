package dev.skullition.lockium.client;

import dev.skullition.lockium.model.GrowtopiaDetail;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
* Instance of {@link org.springframework.web.client.RestClient} to call the Growtopia API.
*/
@HttpExchange
public interface GrowtopiaDetailClient {
  @GetExchange
  GrowtopiaDetail getGrowtopiaDetail();
}
