package com.particle.util.placeholder.addon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CompileThreadBinder {
    CompileThreadType type() default CompileThreadType.ALL;
}
