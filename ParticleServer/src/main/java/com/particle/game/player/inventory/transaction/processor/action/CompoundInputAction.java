package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.game.player.craft.CraftService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.player.Player;

public class CompoundInputAction extends InventoryAction {

    private CraftService craftService;

    public CompoundInputAction(CraftService craftService, InventoryActionData inventoryActionData) {
        super(inventoryActionData);
        this.craftService = craftService;
    }

    @Override
    public boolean isValid(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        return true;
    }

    @Override
    public boolean execute(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        // 记录玩家合成的消耗，并在CompoundOutputAction时检查
        this.craftService.addInputItem(player, this.getToItem(), getSlot());

        return true;
    }

    @Override
    public void onSuccess(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }

    @Override
    public void onFailed(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }
}
