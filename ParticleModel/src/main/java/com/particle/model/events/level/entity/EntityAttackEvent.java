package com.particle.model.events.level.entity;

import com.particle.model.entity.Entity;

public class EntityAttackEvent extends LevelEntityEvent {

    private float damage;
    private Entity victim;

    public EntityAttackEvent(Entity entity, Entity victim, float damage) {
        super(entity);
        this.victim = victim;
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public Entity getVictim() {
        return victim;
    }
}
