package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.game.block.enchantment.AnvilService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

public class AnvilOutputAction extends InventoryAction {

    private AnvilService anvilService;

    public AnvilOutputAction(Inventory inventory, InventoryActionData inventoryActionData, AnvilService anvilService) {
        super(inventory, inventoryActionData);
        this.anvilService = anvilService;
    }

    @Override
    public boolean isValid(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        ItemStack createItem = this.anvilService.processAnvilRepaire(player, this.getInventory());

        if (createItem == null) {
            return false;
        }

        ItemStack fromItem = this.getFromItem();

        // 物品ID，meta和附魔均匹配成功，则放过
        if (fromItem.getItemType() == createItem.getItemType()
                && fromItem.equalsEnchantments(createItem)
                && fromItem.getCount() == createItem.getCount()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean execute(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        // 合成成功获取物品时需要清除所有数据，避免重复刷
        inventoryServiceProxy.clearAll(this.getInventory());
        inventoryServiceProxy.notifyPlayerContentChanged(this.getInventory());

        return true;
    }

    @Override
    public void onSuccess(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }

    @Override
    public void onFailed(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {

    }
}
