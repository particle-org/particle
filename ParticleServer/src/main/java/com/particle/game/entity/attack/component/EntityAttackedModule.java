package com.particle.game.entity.attack.component;

import com.particle.api.attack.IEntityAttackedHandle;
import com.particle.core.ecs.module.BehaviorModule;

public class EntityAttackedModule extends BehaviorModule {

    private IEntityAttackedHandle entityAttackedHandle;
    private long lastDamager;
    private long lastdamageTimestamp;

    public long getLastDamager() {
        return lastDamager;
    }

    public void setLastDamager(long lastDamager) {
        this.lastDamager = lastDamager;
    }

    public long getLastdamageTimestamp() {
        return lastdamageTimestamp;
    }

    public void setLastdamageTimestamp(long lastdamageTimestamp) {
        this.lastdamageTimestamp = lastdamageTimestamp;
    }

    public IEntityAttackedHandle getEntityAttackedHandle() {
        return entityAttackedHandle;
    }

    public void setEntityAttackedHandle(IEntityAttackedHandle entityAttackedHandle) {
        this.entityAttackedHandle = entityAttackedHandle;
    }
}
