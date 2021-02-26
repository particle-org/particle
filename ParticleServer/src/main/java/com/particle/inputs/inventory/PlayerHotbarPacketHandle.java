package com.particle.inputs.inventory;

import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.world.map.MapService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.PlayerHotbarPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerHotbarPacketHandle extends PlayerPacketHandle<PlayerHotbarPacket> {

    private static final Logger logger = LoggerFactory.getLogger(PlayerHotbarPacketHandle.class);

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private InventoryAPIProxy inventoryServiceProxy;

    @Inject
    private MapService mapService;

    @Override
    protected void handlePacket(Player player, PlayerHotbarPacket packet) {
        logger.info("PlayerHotbarPacket:{}", packet);
        int containId = packet.getContainerId();
        if (containId != InventoryConstants.CONTAINER_ID_PLAYER) {
            logger.error("该操作只支持玩家背包");
            return;
        }
        PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, containId);
        if (inventory == null) {
            logger.error(String.format("玩家[%s]背包不存在！", player));
            return;
        }
        inventoryServiceProxy.equipItem(inventory, packet.getSelectedSlot(), null, false);
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.PLAYER_HOTBAR_PACKET;
    }
}
