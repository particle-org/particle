package com.particle.model.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Plugin {

    /**
     * 插件的id，必须唯一
     */
    String id();

    /**
     * 插件的显示名称
     */
    String name();

    /**
     * 插件的版本
     */
    String version() default "1.0.0";

    /**
     * 插件介绍
     */
    String description() default "";
}
