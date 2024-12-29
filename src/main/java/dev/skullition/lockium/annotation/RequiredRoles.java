package dev.skullition.lockium.annotation;

import dev.skullition.lockium.filter.RequiredRolesFilter;
import io.github.freya022.botcommands.api.commands.annotations.Filter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotation to make a guild command only usable by people with certain role(s). */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Filter(RequiredRolesFilter.class)
public @interface RequiredRoles {
  /** The allowed roles' snowflake id. */
  long[] value();
}
