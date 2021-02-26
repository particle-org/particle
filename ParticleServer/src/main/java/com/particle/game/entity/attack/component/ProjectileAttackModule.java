package com.particle.game.entity.attack.component;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.item.ItemStack;

public class ProjectileAttackModule extends BehaviorModule {
    private ItemStack weapon;
    private float baseDamage = 1f;
    private float additionDamage = 8f;
    private float additionDamageSpeed = 15;
    private boolean hasExplosion = false;
    private float explosionDamage = 0f;

    public ItemStack getWeapon() {
        return weapon;
    }

    public void setWeapon(ItemStack weapon) {
        this.weapon = weapon;
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    public float getAdditionDamage() {
        return additionDamage;
    }

    public void setAdditionDamage(float additionDamage) {
        this.additionDamage = additionDamage;
    }

    public float getAdditionDamageSpeed() {
        return additionDamageSpeed;
    }

    public void setAdditionDamageSpeed(float additionDamageSpeed) {
        this.additionDamageSpeed = additionDamageSpeed;
    }

    public boolean isHasExplosion() {
        return hasExplosion;
    }

    public void setHasExplosion(boolean hasExplosion) {
        this.hasExplosion = hasExplosion;
    }

    public float getExplosionDamage() {
        return explosionDamage;
    }

    public void setExplosionDamage(float explosionDamage) {
        this.explosionDamage = explosionDamage;
    }
}
