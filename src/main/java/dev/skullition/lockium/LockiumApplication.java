package dev.skullition.lockium;

import dev.skullition.lockium.annotation.EnableLockiumBot;
import dev.skullition.lockium.config.SecretsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableLockiumBot
@SpringBootApplication
@EnableConfigurationProperties(SecretsConfig.class)
public class LockiumApplication {

    public static void main(String[] args) {
        SpringApplication.run(LockiumApplication.class, args);
    }

}
