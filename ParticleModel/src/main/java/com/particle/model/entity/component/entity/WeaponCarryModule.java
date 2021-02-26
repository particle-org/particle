package com.particle.model.entity.component.entity;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.item.ItemStack;

public class WeaponCarryModule extends BehaviorModule {
    private ItemStack weapon;

    public ItemStack getWeapon() {
        return weapon;
    }

    public void setWeapon(ItemStack weapon) {
        this.weapon = weapon;
    }
}
