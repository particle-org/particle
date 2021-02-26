package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.state.EntityStateService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityStateDisableAction implements IAction {

    @Inject
    private EntityStateService entityStateService;

    private String entityState;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        if (entityState == null) {
            return EStatus.FAILURE;
        }

        this.entityStateService.disableState(entity, entityState);
        return EStatus.SUCCESS;
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