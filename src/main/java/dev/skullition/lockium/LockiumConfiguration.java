package dev.skullition.lockium;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

/** Configuration class for Lockium. */
@Configuration
public class LockiumConfiguration {
  private static final String API_MIME_TYPE = "application/json";
  @Value("${growtopia.sinister-url}")
  private String sinisterUrl;

  @Bean
  public WebClient sinisterWebClient(WebClient.Builder builder) {
    return builder.baseUrl(sinisterUrl).defaultHeader(HttpHeaders.ACCEPT, API_MIME_TYPE).build();
  }
}
