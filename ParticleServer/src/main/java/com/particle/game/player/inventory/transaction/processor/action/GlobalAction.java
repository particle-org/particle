package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.player.Player;

@Deprecated
public class GlobalAction extends InventoryAction {

    public GlobalAction(InventoryActionData inventoryActionData) {
        super(inventoryActionData);
    }

    @Override
    public boolean isValid(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        return false;
    }

    @Override
    public boolean execute(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        return false;
    }

    @Override
    public void onSuccess(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }

    @Override
    public void onFailed(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }
}
