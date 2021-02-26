package com.particle.game.utils.blueprint.task;

import com.particle.game.utils.blueprint.context.BackgroundContext;

@FunctionalInterface
public interface CheckTask<T extends BackgroundContext> {

    boolean check(T t);

}
