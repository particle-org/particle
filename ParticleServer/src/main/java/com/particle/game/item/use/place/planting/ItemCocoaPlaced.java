package com.particle.game.item.use.place.planting;

import com.particle.game.item.use.place.ItemPlaceBlockProcessor;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.math.BlockFace;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class ItemCocoaPlaced extends ItemPlaceBlockProcessor {

    private int[] faces = new int[]{
            0,
            0,
            0,
            2,
            3,
            1,
    };

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        Block blockAt = this.levelService.getBlockAt(player.getLevel(), itemUseInventoryData.getPosition());
        // 只允许种在丛林木头的侧面
        boolean isAllow = true;
        if (blockAt.getType() != BlockPrototype.LOG && blockAt.getMeta() != 3) {
            isAllow = false;
        }
        BlockFace face = itemUseInventoryData.getFace();
        if (face.getHorizontalIndex() < 0) {
            isAllow = false;
        }
        if (isAllow) {
            super.process(player, itemUseInventoryData, inventoryActionData);
        } else {
            PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            this.playerInventoryService.notifyPlayerSlotChanged(playerInventory, playerInventory.getItemInHandle());
        }
    }

    @Override
    protected Block getBlock(Player player, ItemUseInventoryData itemUseInventoryData) {
        Block block = Block.getBlock(BlockPrototype.COCOA);
        block.setMeta(faces[itemUseInventoryData.getFace().getIndex()]);
        return block;
    }
}
