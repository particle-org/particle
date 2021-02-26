package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.game.player.craft.CraftService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

public class CompoundOutputAction extends InventoryAction {

    private CraftService craftService;

    public CompoundOutputAction(CraftService craftService, InventoryActionData inventoryActionData) {
        super(inventoryActionData);
        this.craftService = craftService;
    }

    @Override
    public boolean isValid(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        ItemStack slotItem = this.craftService.getOutput(player);
        if (slotItem == null) {
            return false;
        }
        return slotItem.equalsWithCounts(this.getFromItem());
    }

    @Override
    public boolean execute(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        // 合成成功获取物品时需要清除所有数据，避免重复刷
        this.craftService.resetCraftData(player);
        return true;
    }

    @Override
    public void onSuccess(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }

    @Override
    public void onFailed(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }
}
