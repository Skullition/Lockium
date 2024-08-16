package dev.skullition.lockium.configuration;

import java.time.Instant;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class JdaConfiguration {
  // never autowire this bean
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  @Bean
  public EmbedBuilder embedBuilder(GenericInteractionCreateEvent event) {
    User user = event.getUser();
    return new EmbedBuilder()
        .setColor(14025328)
        .setFooter("Requested by " + user.getName(), user.getAvatarUrl())
        .setTimestamp(Instant.now());
  }
}
