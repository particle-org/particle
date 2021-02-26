package com.particle.game.block.enchantment;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.item.ItemStack;

public class EnchantmentModule extends BehaviorModule {
    private ItemStack currentEnchantItem = null;

    public ItemStack getCurrentEnchantItem() {
        return currentEnchantItem;
    }

    public void setCurrentEnchantItem(ItemStack currentEnchantItem) {
        this.currentEnchantItem = currentEnchantItem;
    }
}
