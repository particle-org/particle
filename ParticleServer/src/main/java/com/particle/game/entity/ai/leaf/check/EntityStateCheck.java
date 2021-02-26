package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.state.EntityStateService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityStateCheck implements ICondition {

    @Inject
    private EntityStateService entityStateService;

    private String entityState;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        if (this.entityState == null) {
            return EStatus.FAILURE;
        }

        if (this.entityStateService.hasState(entity, this.entityState)) {
            return EStatus.SUCCESS;
        } else {
            return EStatus.FAILURE;
        }
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {
        if (key.equalsIgnoreCase("EntityState") && val instanceof String) {
            this.entityState = (String) val;
        }
    }
}
