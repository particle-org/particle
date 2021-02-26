package com.particle.game.entity.attribute.health;

import com.particle.core.ecs.component.ECSComponent;

public class EntityHealthAutoUpdateComponent implements ECSComponent {

    private long lastHealingTime = 0;

    private long minDamageInterval = 2000;

    public long getLastHealingTime() {
        return lastHealingTime;
    }

    public void setLastHealingTime(long lastHealingTime) {
        this.lastHealingTime = lastHealingTime;
    }

    public long getMinDamageInterval() {
        return minDamageInterval;
    }

    public void setMinDamageInterval(long minDamageInterval) {
        this.minDamageInterval = minDamageInterval;
    }
}
