package dev.skullition.lockium.service;

import dev.skullition.lockium.config.SecretsConfig;
import io.github.freya022.botcommands.api.core.JDAService;
import io.github.freya022.botcommands.api.core.events.BReadyEvent;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import java.util.Set;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@BService
public class LockiumBot extends JDAService {
  private static final Logger logger = LoggerFactory.getLogger(LockiumBot.class);
  private final SecretsConfig secretsConfig;

  public LockiumBot(@NotNull SecretsConfig secretsConfig) {
    this.secretsConfig = secretsConfig;
  }

  @NotNull
  @Override
  public Set<GatewayIntent> getIntents() {
    return Set.of(
        GatewayIntent.GUILD_MODERATION,
        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
        GatewayIntent.GUILD_INVITES,
        GatewayIntent.GUILD_VOICE_STATES,
        GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.GUILD_MESSAGE_REACTIONS,
        GatewayIntent.DIRECT_MESSAGES,
        GatewayIntent.DIRECT_MESSAGE_REACTIONS,
        GatewayIntent.SCHEDULED_EVENTS,
        GatewayIntent.AUTO_MODERATION_CONFIGURATION,
        GatewayIntent.AUTO_MODERATION_EXECUTION,
        GatewayIntent.GUILD_MESSAGE_POLLS,
        GatewayIntent.DIRECT_MESSAGE_POLLS);
  }

  @NotNull
  @Override
  public Set<CacheFlag> getCacheFlags() {
    return Set.of();
  }

  @Override
  public void createJDA(@NotNull BReadyEvent bReadyEvent, @NotNull IEventManager iEventManager) {
    logger.info("Creating JDA");
    JDABuilder.createDefault(secretsConfig.token(), getIntents())
        .enableCache(getCacheFlags())
        .setActivity(Activity.customStatus("Hello there! :)"))
        .setEventManager(iEventManager)
        .build();
    logger.info("JDA created");
  }
}
