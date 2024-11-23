package dev.skullition.lockium.service.supplier;

import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import io.github.freya022.botcommands.api.core.events.InjectedJDAEvent;
import java.util.Map;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.emoji.ApplicationEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.springframework.stereotype.Service;

/** This service provides access to application emojis fetched from JDA. */
@Service
public class ApplicationEmojiSupplier {
  private Map<String, Emoji> emojiMap;

  /** Instantiates and caches Emojis in a map at app startup. */
  @BEventListener
  public void onApplicationReady(InjectedJDAEvent event) {
    event
        .getJda()
        .retrieveApplicationEmojis()
        .map(
            emojis ->
                emojis.stream()
                    .collect(Collectors.toMap(ApplicationEmoji::getName, emoji -> (Emoji) emoji)))
        .queue(map -> emojiMap = map);
  }

  /**
   * Retrieves an emoji by its name.
   *
   * @param name the application emoji name.
   * @return the emoji instance.
   * @throws IllegalStateException if the provided name does not exist.
   */
  public Emoji getEmojiByName(String name) {
    Emoji emoji = emojiMap.get(name);
    if (emoji == null) {
      throw new IllegalStateException(
          "Expected valid emoji name %s but found null.".formatted(name));
    }
    return emoji;
  }
}
