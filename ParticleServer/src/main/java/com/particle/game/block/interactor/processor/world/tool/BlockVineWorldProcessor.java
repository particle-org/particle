package com.particle.game.block.interactor.processor.world.tool;

import com.particle.game.block.interactor.processor.world.BreakableBlockProcessor;
import com.particle.model.block.Block;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * 藤蔓
 */
@Singleton
public class BlockVineWorldProcessor extends BreakableBlockProcessor {

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        return false;
    }

    @Override
    protected List<ItemStack> onPreDrops(Level level, Player player, Block targetBlock, Vector3 targetPosition, List<ItemStack> blockDrops) {
        PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack hand = this.playerInventoryService.getItem(inventory, inventory.getItemInHandle());
        // 剪刀破坏才掉东西
        if (hand.getItemType() == ItemPrototype.SHEARS) {
            return blockDrops;
        }
        return new ArrayList<>(0);
    }
}
