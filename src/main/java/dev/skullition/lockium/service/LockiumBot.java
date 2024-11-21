package dev.skullition.lockium.service;

import dev.skullition.lockium.config.SecretsConfig;
import io.github.freya022.botcommands.api.core.JDAService;
import io.github.freya022.botcommands.api.core.config.JDAConfiguration;
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
  @NotNull private final SecretsConfig secretsConfig;
  @NotNull private final JDAConfiguration jdaConfiguration;

  public LockiumBot(
      @NotNull SecretsConfig secretsConfig, @NotNull JDAConfiguration jdaConfiguration) {
    this.secretsConfig = secretsConfig;
    this.jdaConfiguration = jdaConfiguration;
  }

  @NotNull
  @Override
  public Set<GatewayIntent> getIntents() {
    return jdaConfiguration.getIntents();
  }

  @NotNull
  @Override
  public Set<CacheFlag> getCacheFlags() {
    return jdaConfiguration.getCacheFlags();
  }

  @Override
  public void createJDA(@NotNull BReadyEvent readyEvent, @NotNull IEventManager eventManager) {
    logger.info("Creating JDA");
    JDABuilder.createDefault(secretsConfig.token(), getIntents())
        .enableCache(getCacheFlags())
        .setActivity(Activity.customStatus("Hello there! :)"))
        .setEventManager(eventManager)
        .build();
    logger.info("JDA created");
  }
}
