package com.particle.game.player.service;

import com.particle.api.entity.IEntityInteractivedHandle;
import com.particle.core.ecs.module.BehaviorModule;

public class EntityInteractedModule extends BehaviorModule {

    private IEntityInteractivedHandle entityInteractivedHandle;

    public IEntityInteractivedHandle getEntityInteractivedHandle() {
        return entityInteractivedHandle;
    }

    public void setEntityInteractivedHandle(IEntityInteractivedHandle entityInteractivedHandle) {
        this.entityInteractivedHandle = entityInteractivedHandle;
    }
}
