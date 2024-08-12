package dev.skullition.lockium.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.context.annotation.ComponentScan;

@Retention(RetentionPolicy.RUNTIME)
@ComponentScan("dev.skullition.lockium")
public @interface EnableLockiumBot {}
