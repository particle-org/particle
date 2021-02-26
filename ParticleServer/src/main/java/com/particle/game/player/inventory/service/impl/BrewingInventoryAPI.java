package com.particle.game.player.inventory.service.impl;

import com.particle.model.inventory.Inventory;
import com.particle.model.item.ItemStack;

import javax.inject.Singleton;

@Singleton
public class BrewingInventoryAPI extends ContainerInventoryAPI {

    @Override
    protected void onSlotChange(Inventory inventory, int index, ItemStack old, ItemStack newItem, boolean notify) {
        super.onSlotChange(inventory, index, old, newItem, notify);

        this.onBrewingSlotChange(inventory, index, old, notify);
    }

    /**
     * 当酿造台的槽发生变更
     *
     * @param inventory
     * @param index
     * @param old
     * @param notify
     */
    private void onBrewingSlotChange(Inventory inventory, int index, ItemStack old, boolean notify) {

    }
}
