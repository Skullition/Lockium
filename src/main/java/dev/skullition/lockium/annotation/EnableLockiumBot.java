package dev.skullition.lockium.annotation;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ComponentScan("dev.skullition.lockium")
public @interface EnableLockiumBot {
}
