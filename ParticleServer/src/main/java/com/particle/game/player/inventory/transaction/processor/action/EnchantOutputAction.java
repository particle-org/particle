package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.game.block.enchantment.EnchantmentService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.Player;

/**
 * 附魔台的的输出操作
 */
public class EnchantOutputAction extends InventoryAction {

    private EnchantmentService enchantmentService;

    public EnchantOutputAction(Inventory inventory, InventoryActionData inventoryActionData, EnchantmentService enchantmentService) {
        super(inventory, inventoryActionData);

        this.enchantmentService = enchantmentService;
    }

    @Override
    public boolean isValid(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        ItemStack createItem = this.getToItem();
        ItemStack fromItem = this.getFromItem();

        // 附魔操作，一定有个action，是往虚空中消耗青金石，这个可以作为等级判断
        // 如果Output产出是青金石，则允许（保证input扣除对应的青金石）
        if (createItem.getItemType() == ItemPrototype.DYE && fromItem.getItemType() == ItemPrototype.AIR) {
            short cost = (short) createItem.getCount();
            if (cost == 0) {
                return false;
            }

            this.enchantmentService.enchant(player, cost > 3 ? 3 : cost);
            return true;
        }

        if (createItem.getItemType() != ItemPrototype.DYE) {
            this.enchantmentService.markEnchantItem(player, fromItem);
        }

        if (fromItem.getItemType() == ItemPrototype.ENCHANTED_BOOK && createItem.getItemType() == ItemPrototype.BOOK) {
            return true;
        }

        // 如果output是物品，暂时只判断物品是否一致
        return fromItem.getItemType() == createItem.getItemType() && fromItem.getCount() == createItem.getCount();
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
