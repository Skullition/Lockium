package dev.skullition.lockium;

import io.github.freya022.botcommands.api.core.annotations.EnableBotCommands;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBotCommands
public class LockiumApplication {

    public static void main(String[] args) {
        SpringApplication.run(LockiumApplication.class, args);
    }

}
