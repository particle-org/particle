package com.particle.model.events.level.entity;

import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;

public class ProjectileHitEntityEvent extends LevelEntityEvent {
    private Entity source;
    private Entity victim;
    private Entity projectile;
    private ItemStack weapon;

    public ProjectileHitEntityEvent(Entity source, Entity victim, Entity projectile, ItemStack weapon) {
        super(source);
        this.source = source;
        this.victim = victim;
        this.projectile = projectile;
        this.weapon = weapon;
    }

    public Entity getSource() {
        return source;
    }

    public Entity getVictim() {
        return victim;
    }

    public Entity getProjectile() {
        return projectile;
    }

    public ItemStack getWeapon() {
        return weapon;
    }
}
