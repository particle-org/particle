package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.player.Player;

/**
 * 关闭合成台时，物品返回背包action
 */
public class WorkBenchClearAction extends InventoryAction {

    public WorkBenchClearAction(Inventory inventory, InventoryActionData inventoryActionData) {
        super(inventory, inventoryActionData);
    }

    @Override
    public boolean isValid(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        return true;
    }

    @Override
    public boolean execute(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        return true;
    }

    @Override
    public void onSuccess(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }

    @Override
    public void onFailed(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }
}
