package dev.skullition.lockium;

import dev.skullition.lockium.client.SinisterClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/** Configuration class for Lockium. */
@Configuration
public class LockiumConfiguration {
  @Value("${growtopia.sinister-url}")
  private String sinisterUrl;

  /**
   * Declares bean for calling Sinister internally.
   *
   * @param builder builder to customise {@link RestClient} from.
   * @return a RestClient instance as {@link SinisterClient}.
   */
  @Bean
  public SinisterClient sinisterClient(RestClient.Builder builder) {
    RestClient restClient =  builder
        .baseUrl(sinisterUrl)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build();
    RestClientAdapter adapter = RestClientAdapter.create(restClient);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
    return factory.createClient(SinisterClient.class);
  }
}
