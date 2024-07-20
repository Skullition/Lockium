package dev.skullition.lockium.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("secrets")
public record SecretsConfig(String token, List<String> ownerIds) {
}
