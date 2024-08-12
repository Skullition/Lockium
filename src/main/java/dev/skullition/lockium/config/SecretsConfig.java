package dev.skullition.lockium.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("secrets")
public record SecretsConfig(@NotNull String token) {
}
