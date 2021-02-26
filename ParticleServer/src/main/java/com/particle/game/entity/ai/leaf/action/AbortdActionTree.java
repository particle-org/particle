package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.model.entity.Entity;

public class AbortdActionTree implements IAction {
    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        return EStatus.ABORTED;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {

    }
}
