package com.particle.api.item;

import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemReleaseInventoryData;
import com.particle.model.player.Player;

public interface IItemReleaseProcessor {
    void process(Player player, ItemReleaseInventoryData itemReleaseInventoryData, InventoryActionData[] inventoryActionData);
}
