package com.particle.game.entity.attribute.health;

import com.particle.core.ecs.component.ECSComponent;

public class EntityHealthComponent implements ECSComponent {

    private float health = 20.0f;

    private float maxHealth = 20.0f;

    private float absorption = 0f; // 护盾

    private float maxAbsorption = 4.0f;

    private long lastDamageTime = 0;

    private long minDamageInterval = 500;

    private boolean dead = false;

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getAbsorption() {
        return absorption;
    }

    public void setAbsorption(float absorption) {
        this.absorption = absorption;
    }

    public float getMaxAbsorption() {
        return maxAbsorption;
    }

    public void setMaxAbsorption(float maxAbsorption) {
        this.maxAbsorption = maxAbsorption;
    }

    public long getLastDamageTime() {
        return lastDamageTime;
    }

    public void setLastDamageTime(long lastDamageTime) {
        this.lastDamageTime = lastDamageTime;
    }

    public long getMinDamageInterval() {
        return minDamageInterval;
    }

    public void setMinDamageInterval(long minDamageInterval) {
        this.minDamageInterval = minDamageInterval;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
