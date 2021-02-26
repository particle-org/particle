package com.particle.model.events.level.entity;

import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

public class EntityDamageByEntityEvent extends LevelEntityEvent {

    private float damage;
    private Vector3f knockback;
    private Entity damager;

    public EntityDamageByEntityEvent(Entity victim) {
        super(victim);
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Vector3f getKnockback() {
        return knockback;
    }

    public void setKnockback(Vector3f knockback) {
        this.knockback = knockback;
    }

    public Entity getDamager() {
        return damager;
    }

    public void setDamager(Entity damager) {
        this.damager = damager;
    }
}
