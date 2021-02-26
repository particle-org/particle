package com.particle.game.entity.attack.component;

import com.particle.core.ecs.module.BehaviorModule;

public class EntityAttackModule extends BehaviorModule {
    private float baseDamage = 1f;
    private long attackInterval;
    private long lastAttackTimestamp;

    public boolean canAttack() {
        return System.currentTimeMillis() - this.getLastAttackTimestamp() >= this.getAttackInterval();
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

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
}
