package com.particle.game.item.use.place.planting;

import com.particle.game.item.use.place.ItemPlaceBlockProcessor;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Singleton;

/**
 * 花朵生长
 */
@Singleton
public class ItemFlowerPlaced extends ItemPlaceBlockProcessor {

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        Vector3 vector3 = itemUseInventoryData.getPosition();
        BlockPrototype targetBlock = this.levelService.getBlockTypeAt(player.getLevel(), vector3);
        if (targetBlock == null) {
            return;
        }
        BlockPrototype blockPrototype = targetBlock;
        // 只允许种在草方块、泥土、耕地方块上
        if (blockPrototype == BlockPrototype.GRASS || blockPrototype == BlockPrototype.DIRT ||
                blockPrototype == BlockPrototype.FARMLAND) {
            super.process(player, itemUseInventoryData, inventoryActionData);
        } else {
            PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            this.playerInventoryService.notifyPlayerSlotChanged(playerInventory, playerInventory.getItemInHandle());
        }

    }

    @Override
    protected Block getBlock(Player player, ItemUseInventoryData itemUseInventoryData) {
        return super.getBlock(player, itemUseInventoryData);
    }
}
