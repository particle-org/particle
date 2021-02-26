package com.particle.game.block.interactor.processor.world.tool;

import com.particle.game.block.interactor.processor.world.BreakableBlockProcessor;
import com.particle.model.block.Block;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemTag;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * 炼药锅
 */
@Singleton
public class BlockCauldronWorldProcessor extends BreakableBlockProcessor {

    @Override
    protected List<ItemStack> onPreDrops(Level level, Player player, Block targetBlock, Vector3 targetPosition, List<ItemStack> blockDrops) {
        PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack hand = this.playerInventoryService.getItem(inventory, inventory.getItemInHandle());
        // 镐子破坏才掉东西
        if (hand.getItemType().hasTag(ItemTag.TOOL_PICKAXE)) {
            return blockDrops;
        }
        return new ArrayList<>(0);
    }
}
