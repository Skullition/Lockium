package dev.skullition.lockium;

import dev.skullition.lockium.client.GrowtopiaDetailClient;
import dev.skullition.lockium.client.SinisterClient;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/** Configuration class for Lockium. */
@Configuration
public class LockiumConfiguration {
  @Value("${growtopia.sinister-url}")
  private String sinisterUrl;

  @Value("${growtopia.detail-url}")
  private String growtopiaDetailUrl;

  /**
   * Declares bean for calling Sinister internally.
   *
   * @param builder builder to customise {@link RestClient} from.
   * @return a RestClient instance as {@link SinisterClient}.
   */
  @Bean
  public SinisterClient sinisterClient(RestClient.Builder builder) {
    RestClient restClient =
        builder
            .baseUrl(sinisterUrl)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();
    RestClientAdapter adapter = RestClientAdapter.create(restClient);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
    return factory.createClient(SinisterClient.class);
  }

  /**
   * Declares bean for calling the Growtopia API.
   *
   * @param builder builder to customise {@link RestClient} from.
   * @return a RestClient instance as {@link GrowtopiaDetailClient}.
   */
  @Bean
  public GrowtopiaDetailClient growtopiaDetailClient(RestClient.Builder builder) {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(List.of(MediaType.TEXT_HTML));
    RestClient restClient =
        builder
            .baseUrl(growtopiaDetailUrl)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE)
            .messageConverters(configurer -> configurer.add(converter))
            .build();
    RestClientAdapter adapter = RestClientAdapter.create(restClient);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
    return factory.createClient(GrowtopiaDetailClient.class);
  }
}
