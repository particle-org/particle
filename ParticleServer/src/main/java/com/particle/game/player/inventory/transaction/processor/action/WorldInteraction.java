package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.game.item.ItemDropService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要用于将物品丢弃的action处理
 */
public class WorldInteraction extends InventoryAction {

    private ItemDropService itemDropService;

    public WorldInteraction(InventoryActionData inventoryActionData, ItemDropService itemDropService) {
        super(inventoryActionData);
        this.itemDropService = itemDropService;
    }

    @Override
    public boolean isValid(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        if (this.getToItem().getCount() > this.getToItem().getMaxStackSize()) {
            return false;
        }

        ItemStack fromItem = this.getFromItem();
        return fromItem.isNull();
    }

    @Override
    public boolean execute(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(this.getToItem());

        boolean state = this.itemDropService.playerDropItem(player, drops);

        if (!state) {
            // 如果失败，将物品放回背包中
            Inventory playerInventory = inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            inventoryServiceProxy.addItem(playerInventory, this.getToItem());
        }

        return true;
    }

    @Override
    public void onSuccess(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }

    @Override
    public void onFailed(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }
}
