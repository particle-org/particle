package com.particle.game.entity.attack.component;

import com.particle.core.ecs.module.BehaviorModule;

public class EntityRemoteAttackModule extends BehaviorModule {
    private long attackInterval;
    private long lastAttackTimestamp;

    private float damageRate = 1;

    public long getAttackInterval() {
        return attackInterval;
    }

    public void setAttackInterval(long attackInterval) {
        this.attackInterval = attackInterval;
    }

    public long getLastAttackTimestamp() {
        return lastAttackTimestamp;
    }

    public void setLastAttackTimestamp(long lastAttackTimestamp) {
        this.lastAttackTimestamp = lastAttackTimestamp;
    }

    public float getDamageRate() {
        return damageRate;
    }

    public void setDamageRate(float damageRate) {
        this.damageRate = damageRate;
    }
}
