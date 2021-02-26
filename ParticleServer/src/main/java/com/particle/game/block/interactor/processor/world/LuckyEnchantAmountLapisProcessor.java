package com.particle.game.block.interactor.processor.world;

import com.particle.game.item.ItemAttributeService;
import com.particle.model.block.Block;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantment;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import java.util.List;

public class LuckyEnchantAmountLapisProcessor extends BreakableBlockProcessor {

    @Override
    protected List<ItemStack> onPreDrops(Level level, Player player, Block targetBlock, Vector3 targetPosition, List<ItemStack> blockDrops) {
        // 判断下掉落物
        if (blockDrops.size() == 0) {
            return null;
        }

        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player,
                InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack handItem = this.playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());

        Enchantment enchantment = ItemAttributeService.getEnchantment(handItem, Enchantments.FORTUNE);

        if (enchantment != null) {
            int externalAmount = (int) (Math.random() * enchantment.getLevel() * 24);

            if (externalAmount > 0) {
                ItemStack dropItem = blockDrops.get(0);
                ItemStack itemStack = ItemStack.getItem(dropItem.getItemType(), dropItem.getMeta(), externalAmount);
                blockDrops.add(itemStack);
            }
        }

        return blockDrops;
    }
}
