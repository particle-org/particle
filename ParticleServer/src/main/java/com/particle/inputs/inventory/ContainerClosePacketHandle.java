package com.particle.inputs.inventory;

import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.events.level.player.PlayerCloseCustomContainerEvent;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.ContainerClosePacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ContainerClosePacketHandle extends PlayerPacketHandle<ContainerClosePacket> {

    private static final Logger logger = LoggerFactory.getLogger(ContainerClosePacketHandle.class);

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private InventoryAPIProxy inventoryServiceProxy;

    @Inject
    private NetworkManager networkManager;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Override
    protected void handlePacket(Player player, ContainerClosePacket packet) {
        logger.debug("onClose container, the containId: {}", packet.getContainerId());
        if (!player.isSpawned() || (packet.getContainerId() == 0 && !player.isInventoryOpen())) {
            logger.warn("处理玩家{}, 关背包的操作错误", player.getIdentifiedStr());
            return;
        }
        // 当合成台关闭时候，此时的containId为-1
        // 关闭玩家背包的时候，containId也是-1
        ContainerClosePacket containerClosePacket = new ContainerClosePacket();
        containerClosePacket.setContainerId(packet.getContainerId());
        this.inventoryManager.setOpenContainerStatus(player, ContainerType.PLAYER);
        if (packet.getContainerId() == InventoryConstants.CONTAINER_ID_NONE) {
            this.inventoryManager.onCloseWorkBench(player);
            networkManager.sendMessage(player.getClientAddress(), containerClosePacket);
            return;
        } else if (packet.getContainerId() == InventoryConstants.CONTAINER_ID_PLAYER) {
            this.inventoryManager.onCloseWorkBench(player);
            player.setInventoryOpen(false);
            networkManager.sendMessage(player.getClientAddress(), containerClosePacket);
            return;
        } else if (packet.getContainerId() == InventoryConstants.CONTAINER_ID_ENDER) {
            // 末影箱是跟随玩家的，不需要讲背包从玩家列表中删除
            this.onCloseEnder(player, packet);

            this.eventDispatcher.dispatchEvent(new PlayerCloseCustomContainerEvent(player, packet.getContainerId()));
            networkManager.sendMessage(player.getClientAddress(), containerClosePacket);
            return;
        } else if (packet.getContainerId() == InventoryConstants.CONTAINER_ID_ANVIL) {
            this.inventoryManager.onCloseAnvil(player);
        } else if (packet.getContainerId() == InventoryConstants.CONTAINER_ID_ENCHANT) {
            this.inventoryManager.onCloseEnchantTable(player);
        }
        Inventory inventory = this.inventoryManager.getInventoryByContainerId(player, packet.getContainerId());
        if (inventory != null) {
            this.inventoryServiceProxy.removeView(player, inventory, true);
            this.inventoryManager.unbindMultiInventory(player, packet.getContainerId());
        }

        networkManager.sendMessage(player.getClientAddress(), containerClosePacket);
        this.eventDispatcher.dispatchEvent(new PlayerCloseCustomContainerEvent(player, packet.getContainerId()));
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.CONTAINER_CLOSE_PACKET;
    }

    /**
     * 关闭末影箱
     *
     * @param player
     * @param packet
     */
    private void onCloseEnder(Player player, ContainerClosePacket packet) {
        Inventory inventory = this.inventoryManager.getInventoryByContainerId(player, packet.getContainerId());
        if (inventory != null) {
            this.inventoryServiceProxy.removeView(player, inventory, true);
        }
    }
}
