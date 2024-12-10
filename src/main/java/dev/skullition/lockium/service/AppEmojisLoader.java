package dev.skullition.lockium.service;

import dev.skullition.lockium.util.AppEmojis;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import io.github.freya022.botcommands.api.core.events.PreFirstGatewayConnectEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.emoji.ApplicationEmoji;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** This service provides access to application emojis fetched from JDA. */
@Service
public class AppEmojisLoader {
  private static final Map<String, ApplicationEmoji> emojis = new HashMap<>();
  private static final Logger logger = LoggerFactory.getLogger(AppEmojisLoader.class);

  /**
   * Retrieves an emoji by its name.
   *
   * @param emojiName the application emoji name (Expected format is lower_snake_case)
   * @return the emoji instance
   */
  @NotNull
  public static ApplicationEmoji getEmoji(String emojiName) {
    // could return null, but only happens if emoji name doesn't exist. In that case we should throw
    // anyway.
    return emojis.remove(emojiName);
  }

  @NotNull
  private ApplicationEmoji uploadEmoji(
      PreFirstGatewayConnectEvent event, ScanResult scan, String discordEmojiName) {
    logger.info("Application emoji {} not found. Registering...", discordEmojiName);

    String resourceLocation = "emojis/%s.**".formatted(discordEmojiName);
    try (InputStream resourceStream =
        scan.getResourcesMatchingWildcard(resourceLocation).getFirst().open()) {
      return event
          .getJda()
          .createApplicationEmoji(discordEmojiName, Icon.from(resourceStream))
          .complete();
    } catch (IOException e) {
      throw new UncheckedIOException(
          "Failed to read emoji resource: %s".formatted(resourceLocation), e);
    }
  }

  /**
   * Instantiates and caches emojis after all services are ready, but before the app connects to the
   * websocket. This is a blocking operation.
   */
  @BEventListener(mode = BEventListener.RunMode.BLOCKING)
  public void onPreGatewayConnect(PreFirstGatewayConnectEvent event) {
    List<ApplicationEmoji> applicationEmojis =
        event.getJda().retrieveApplicationEmojis().complete();

    try (ScanResult scan = new ClassGraph().acceptPackages("emojis").scan()) {
      for (Field field : AppEmojis.class.getDeclaredFields()) {
        String discordEmojiName = field.getName().toLowerCase();

        var appEmoji =
            applicationEmojis.stream()
                .filter(emoji -> emoji.getName().equals(discordEmojiName))
                .findAny()
                .orElseGet(() -> uploadEmoji(event, scan, discordEmojiName));

        emojis.put(discordEmojiName, appEmoji);
      }
    }
  }
}
