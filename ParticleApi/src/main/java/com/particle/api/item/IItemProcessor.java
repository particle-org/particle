package com.particle.api.item;

import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.player.Player;

public interface IItemProcessor {
    void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData);
}
