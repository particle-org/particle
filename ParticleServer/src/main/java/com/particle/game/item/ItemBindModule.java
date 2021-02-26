package com.particle.game.item;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.item.ItemStack;

public class ItemBindModule extends BehaviorModule {
    private ItemStack item;

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
