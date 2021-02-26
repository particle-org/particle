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

import java.util.LinkedList;
import java.util.List;

public class LuckyEnchantProbilityBlockProcessor extends BreakableBlockProcessor {

    @Override
    protected List<ItemStack> onPreDrops(Level level, Player player, Block targetBlock, Vector3 targetPosition, List<ItemStack> blockDrops) {

        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player,
                InventoryConstants.CONTAINER_ID_PLAYER);

        ItemStack handItem = this.playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());

        Enchantment enchantment = ItemAttributeService.getEnchantment(handItem, Enchantments.FORTUNE);

        if (enchantment != null) {
            switch (enchantment.getLevel()) {
                case 1:
                    if (Math.random() < 0.33) {
                        return this.multipleDroped(level, blockDrops, 1, targetPosition);
                    }
                    break;
                case 2:
                    if (Math.random() < 0.25) {
                        return this.multipleDroped(level, blockDrops, 1, targetPosition);
                    } else if (Math.random() < 0.5) {
                        return this.multipleDroped(level, blockDrops, 2, targetPosition);
                    }
                    break;
                case 3:
                    if (Math.random() < 0.2) {
                        return this.multipleDroped(level, blockDrops, 1, targetPosition);
                    } else if (Math.random() < 0.4) {
                        return this.multipleDroped(level, blockDrops, 2, targetPosition);
                    } else if (Math.random() < 0.6) {
                        return this.multipleDroped(level, blockDrops, 3, targetPosition);
                    }
                    break;
            }
        }

        return blockDrops;
    }

    private List<ItemStack> multipleDroped(Level level, List<ItemStack> blockDrops, int rate, Vector3 targetPosition) {
        List<ItemStack> itemStacks = new LinkedList<>();

        for (int i = 0; i < rate; i++) {
            for (ItemStack itemStack : blockDrops) {
                itemStacks.add(ItemStack.getItem(itemStack.getItemType(), itemStack.getCount()));
            }
        }
        blockDrops.addAll(itemStacks);
        return blockDrops;
    }
}
