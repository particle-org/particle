package com.particle.game.player.inventory.service.impl;

import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.network.packets.data.InventorySlotPacket;
import com.particle.model.player.Player;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class PlayerCursorInventoryAPI extends CommonInventoryAPI {

    @Override
    public void notifyPlayerSlotChanged(Inventory inventory, int index) {
        this.notifyPlayerCursorSlotChanged(inventory, index, inventory.getViewers());
    }

    /**
     * 通知单格背包的槽有变化
     *
     * @param inventory
     * @param index
     * @param players
     */
    private void notifyPlayerCursorSlotChanged(Inventory inventory, int index, Collection<Player> players) {
        for (Player player : players) {
            InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
            inventorySlotPacket.setItemStack(this.getItem(inventory, index));
            inventorySlotPacket.setSlot(index);
            if (player.getRuntimeId() == inventory.getInventoryHolder().getRuntimeId()) {
                inventorySlotPacket.setContainerId(InventoryConstants.CONTAINER_ID_PLAYER_UI);
            } else {
                int containId = this.inventoryManager.getMapIdFromMultiOwned(player, inventory);
                if (containId == -1) {
                    this.removeView(player, inventory, true);
                    continue;
                }
                inventorySlotPacket.setContainerId(containId);
            }

            networkManager.sendMessage(player.getClientAddress(), inventorySlotPacket);
        }
    }
}
