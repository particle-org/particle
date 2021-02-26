package com.particle.model.events.level.entity;

import com.particle.model.entity.Entity;

public class EntityDamagedEvent extends LevelEntityEvent {
    private float damage;
    private EntityDamageType damageType;
    private Entity damager; // 可能为null

    public EntityDamagedEvent(Entity entity, float damage, EntityDamageType damageType, Entity damager) {
        super(entity);
        this.damage = damage;
        this.damageType = damageType;
        this.damager = damager;
    }

    public float getDamage() {
        return damage;
    }

    public EntityDamageType getDamageType() {
        return damageType;
    }

    public Entity getDamager() {
        return damager;
    }
}
