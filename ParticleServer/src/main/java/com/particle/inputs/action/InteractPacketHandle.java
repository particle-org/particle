package com.particle.inputs.action;

import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.link.EntityLinkServiceProxy;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.ContainerInventoryAPI;
import com.particle.game.ui.VirtualButtonService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.events.level.player.PlayerInteractActionEvent;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.InteractPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InteractPacketHandle extends PlayerPacketHandle<InteractPacket> {

    private static final Logger logger = LoggerFactory.getLogger(InteractPacketHandle.class);

    @Inject
    private VirtualButtonService virtualButtonService;

    @Inject
    private EntityLinkServiceProxy entityLinkServiceProxy;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private ContainerInventoryAPI containerInventoryAPI;

    @Inject
    private InventoryManager inventoryManager;

    @Override
    protected void handlePacket(Player player, InteractPacket packet) {
        logger.debug("player:{}, InteractPacketHandle:{}", player.getIdentifiedStr(), packet);
        PlayerInteractActionEvent playerInteractActionEvent = new PlayerInteractActionEvent(player,
                packet.getInteractPacketAction(), packet.getTargetRuntimeId(), packet.getPosition());
        this.eventDispatcher.dispatchEvent(playerInteractActionEvent);

        switch (packet.getInteractPacketAction()) {
            case StopRiding:
                this.entityLinkServiceProxy.unRideEntity(player);
                break;
            case InteractUpdate:
                virtualButtonService.refreshButtonName(player, packet.getTargetRuntimeId());
                break;
            case NpcOpen:
                break;
            case OpenInventory:
                if (!player.isInventoryOpen()) {
                    PlayerInventory inventory = (PlayerInventory) inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
                    containerInventoryAPI.addView(player, inventory);
                    player.setInventoryOpen(true);
                }
                break;
        }
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.INTERACT_PACKET;
    }
}
