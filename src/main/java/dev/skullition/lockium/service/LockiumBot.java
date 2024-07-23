package dev.skullition.lockium.service;

import dev.skullition.lockium.config.SecretsConfig;
import io.github.freya022.botcommands.api.core.JDAService;
import io.github.freya022.botcommands.api.core.events.BReadyEvent;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@BService
public class LockiumBot extends JDAService {
    private final SecretsConfig secretsConfig;
    private static final Logger logger = LoggerFactory.getLogger(LockiumBot.class);

    public LockiumBot(SecretsConfig secretsConfig) {
        this.secretsConfig = secretsConfig;
    }

    @NotNull
    @Override
    public Set<GatewayIntent> getIntents() {
        return Set.of();
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
