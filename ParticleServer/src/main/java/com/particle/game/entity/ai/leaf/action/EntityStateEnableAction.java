package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.state.EntityStateService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityStateEnableAction implements IAction {

    @Inject
    private EntityStateService entityStateService;

    private String entityState;
    private long updateInterval = 500;
    private long ttl = -1;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        if (this.entityState == null) {
            return EStatus.FAILURE;
        }

        if (this.entityStateService.hasState(entity, this.entityState)) {
            return EStatus.FAILURE;

        }

        this.entityStateService.enableState(entity, this.entityState, updateInterval, ttl);
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
        } else if (key.equalsIgnoreCase("updateInterval") && val instanceof Long) {
            this.updateInterval = (Long) val;
        } else if (key.equalsIgnoreCase("ttl") && val instanceof Long) {
            this.ttl = (Long) val;
        }
    }
}