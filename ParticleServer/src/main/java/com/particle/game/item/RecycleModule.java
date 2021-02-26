package com.particle.game.item;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;

public class RecycleModule extends BehaviorModule {
    private int recycleTick;

    private Entity entity;

    private ItemStack weapon;

    private int enchantmentLevel;

    public int getRecycleTick() {
        return recycleTick;
    }

    public void setRecycleTick(int recycleTick) {
        this.recycleTick = recycleTick;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public int getEnchantmentLevel() {
        return enchantmentLevel;
    }

    public void setEnchantmentLevel(int enchantmentLevel) {
        this.enchantmentLevel = enchantmentLevel;
    }

    public ItemStack getWeapon() {
        return weapon;
    }

    public void setWeapon(ItemStack weapon) {
        this.weapon = weapon;
    }
}
