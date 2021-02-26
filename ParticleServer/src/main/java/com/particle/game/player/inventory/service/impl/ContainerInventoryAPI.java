package com.particle.game.player.inventory.service.impl;

import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.network.packets.data.ContainerClosePacket;
import com.particle.model.network.packets.data.ContainerOpenPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理除玩家背包（PlayerInventory）、单格背包(PlayerCursor)、末影箱（PlayerEnder）、装备背包、副手背包外的其他背包
 * <p>
 * 业务不直接持有该对象，可通过InventoryServiceProxy代理
 */
public class ContainerInventoryAPI extends BaseInventoryAPI {

    private static final Logger logger = LoggerFactory.getLogger(ContainerInventoryAPI.class);

    @Override
    public void addView(Player player, Inventory inventory) {
        super.addView(player, inventory);
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setContainerId(this.inventoryManager.getMapIdFromMultiOwned(player, inventory));
        containerOpenPacket.setContainType(inventory.getContainerType().getType());

        InventoryHolder holder = inventory.getInventoryHolder();
        containerOpenPacket.setX(holder.getPosition().getFloorX());
        containerOpenPacket.setY(holder.getPosition().getFloorY());
        containerOpenPacket.setZ(holder.getPosition().getFloorZ());
        networkManager.sendMessage(player.getClientAddress(), containerOpenPacket);
        this.notifyPlayerContentChanged(inventory);
    }

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
