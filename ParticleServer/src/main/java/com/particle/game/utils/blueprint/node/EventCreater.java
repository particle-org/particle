package com.particle.game.utils.blueprint.node;

import com.particle.game.utils.blueprint.context.BackgroundContext;
import com.particle.model.events.level.LevelEvent;

@FunctionalInterface
public interface EventCreater<T extends BackgroundContext> {
    LevelEvent create(T context);
}
