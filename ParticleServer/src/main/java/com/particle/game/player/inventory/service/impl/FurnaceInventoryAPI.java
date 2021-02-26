package com.particle.game.player.inventory.service.impl;

import com.particle.model.inventory.Inventory;
import com.particle.model.item.ItemStack;

import javax.inject.Singleton;

@Singleton
public class FurnaceInventoryAPI extends ContainerInventoryAPI {

    @Override
    protected void onSlotChange(Inventory inventory, int index, ItemStack old, ItemStack newItem, boolean notify) {
        super.onSlotChange(inventory, index, old, newItem, notify);

    }

    /**
     * 当熔炉的槽发生变化时
     *
     * @param inventory
     * @param index
     * @param old
     * @param notify
     */
    protected void onFurnaceSlotChange(Inventory inventory, int index, ItemStack old, boolean notify) {
    }
}
