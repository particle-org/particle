package com.particle.game.item.use.place.planting;

import com.particle.game.item.use.place.ItemPlaceBlockProcessor;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class ItemDoubleFlowerPlaced extends ItemPlaceBlockProcessor {

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        Vector3 vector3 = itemUseInventoryData.getPosition();
        BlockPrototype downBlock = this.levelService.getBlockTypeAt(player.getLevel(), vector3);
        if (downBlock == null) {
            return;
        }
        BlockPrototype blockPrototype = downBlock;
        // 只允许种在草方块、泥土、耕地方块上
        if (blockPrototype != BlockPrototype.GRASS && blockPrototype != BlockPrototype.DIRT) {
            PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            this.playerInventoryService.notifyPlayerSlotChanged(playerInventory, playerInventory.getItemInHandle());
            return;
        }
        Vector3 upPosition = vector3.up(2);
        BlockPrototype upBlock = this.levelService.getBlockTypeAt(player.getLevel(), upPosition);
        if (upBlock == null || upBlock == BlockPrototype.AIR) {
            super.process(player, itemUseInventoryData, inventoryActionData);
        } else {
            PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            this.playerInventoryService.notifyPlayerSlotChanged(playerInventory, playerInventory.getItemInHandle());
        }
    }
}
