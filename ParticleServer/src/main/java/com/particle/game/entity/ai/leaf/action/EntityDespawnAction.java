package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityDespawnAction implements IAction {
    @Inject
    private EntitySpawnService entitySpawnService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        entitySpawnService.despawn(entity);
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

    }
}