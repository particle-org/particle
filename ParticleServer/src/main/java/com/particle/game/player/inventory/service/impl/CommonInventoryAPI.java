package com.particle.game.player.inventory.service.impl;

import com.particle.model.inventory.Inventory;
import com.particle.model.network.packets.data.ContainerClosePacket;
import com.particle.model.player.Player;

/**
 * 处理玩家cursor（单格背包），ender(末影箱背包)的基础类
 */
public abstract class CommonInventoryAPI extends BaseInventoryAPI {

    @Override
    public void removeView(Player player, Inventory inventory, boolean serverSide) {
        if (serverSide) {
            ContainerClosePacket containerClosePacket = new ContainerClosePacket();
            containerClosePacket.setContainerId(this.inventoryManager.getMapIdFromMultiOwned(player, inventory));
            networkManager.sendMessage(player.getClientAddress(), containerClosePacket);
        }
        super.removeView(player, inventory, serverSide);
    }
}
