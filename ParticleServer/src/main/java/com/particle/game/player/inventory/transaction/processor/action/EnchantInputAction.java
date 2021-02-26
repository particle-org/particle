package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.game.block.enchantment.EnchantmentService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.player.Player;

public class EnchantInputAction extends ContainerChangeAction {

    private EnchantmentService enchantmentService;

    public EnchantInputAction(Inventory inventory, InventoryActionData actionData, EnchantmentService enchantmentService) {
        super(inventory, actionData);

        this.enchantmentService = enchantmentService;
    }

    /**
     * 执行附魔方法
     * <p>
     * 这里实际上并不是按照客户端上传的物品set，而是不执行任何操作，等待服务端主动附魔
     *
     * @return
     */
    public boolean execute(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        // 如果是附魔物品，则不执行更改操作，不信任客户端
        if (this.enchantmentService.checkEnchantItem(player, this.getToItem())) {
            this.enchantmentService.resetEnchantItem(player);

            return true;

            // this.getToItem().removeEnchantments();
        }

        return super.execute(inventoryManager, inventoryServiceProxy, player);
    }
}
