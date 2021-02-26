package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.modules.PlayerUIInventoryModule;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.Player;

public class PlayerUIOutputAction extends InventoryAction {

    private static final ECSModuleHandler<PlayerUIInventoryModule> PLAYER_UI_INVENTORY_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerUIInventoryModule.class);

    /**
     * 预执行的列表
     */
    private PlayerUIInventoryModule playerUIInventoryModule = null;

    public void setPlayerUIInventoryModule(PlayerUIInventoryModule playerUIInventoryModule) {
        this.playerUIInventoryModule = playerUIInventoryModule;
    }

    public PlayerUIOutputAction(Inventory inventory, InventoryActionData inventoryActionData) {
        super(inventory, inventoryActionData);
    }

    @Override
    public boolean isValid(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        if (this.getFromItem().getItemType() != ItemPrototype.AIR) {
            PlayerUIInventoryModule playerUIModule = this.playerUIInventoryModule;
            if (playerUIModule == null) {
                playerUIModule = PLAYER_UI_INVENTORY_MODULE_HANDLER.getModule(player);
            }


            return playerUIModule.hasItem(this.getFromItem());
        }

        return true;
    }

    @Override
    public boolean execute(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        if (this.getFromItem().getItemType() != ItemPrototype.AIR) {
            PlayerUIInventoryModule playerUIModule = this.playerUIInventoryModule;
            if (playerUIModule == null) {
                playerUIModule = PLAYER_UI_INVENTORY_MODULE_HANDLER.getModule(player);
            }
            // 如果输出包含多笔，区分处理
            ItemStack fromItem = this.getFromItem();
            ItemStack toItem = this.getToItem();
            if (fromItem.equalsAll(toItem)) {
                return playerUIModule.
                        removeItem(ItemStack.getItem(fromItem.getItemType(), fromItem.getCount() - toItem.getCount()));
            } else {
                return playerUIModule.removeItem(this.getFromItem());
            }

        } else if (this.getToItem().getItemType() != ItemPrototype.AIR) {
            PlayerUIInventoryModule playerUIModule = this.playerUIInventoryModule;
            if (playerUIModule == null) {
                playerUIModule = PLAYER_UI_INVENTORY_MODULE_HANDLER.getModule(player);
            }

            playerUIModule.addItem(this.getToItem());

            return true;
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
