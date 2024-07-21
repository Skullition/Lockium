package dev.skullition.lockium.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("secrets")
public record SecretsConfig(String token) {
}
