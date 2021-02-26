package com.particle.util.loader.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    String key();

    String alias() default "";

    int minItems() default 0;

    int maxItems() default 2147483647;

    int maxLength() default 2147483647;

    int minLength() default -2147483648;

    String pattern() default "";

    long minimum() default -9223372036854775808L;

    long maximum() default 9223372036854775807L;
}
