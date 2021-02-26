package com.particle.game.utils.blueprint.task;

import com.particle.game.utils.blueprint.context.BackgroundContext;

@FunctionalInterface
public interface JobTask<T extends BackgroundContext> {

    void job(T t);

}
