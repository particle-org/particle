package com.particle.game.utils.blueprint.node;

import com.particle.game.utils.blueprint.context.BackgroundContext;

@FunctionalInterface
public interface Condition<T extends BackgroundContext> {
    boolean test(T t);
}