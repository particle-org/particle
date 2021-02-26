package com.particle.game.block.interactor.processor.interactive;

import com.particle.model.block.Block;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.math.Vector3;
import com.particle.model.network.packets.data.ContainerOpenPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ContainerBlockCraftingTableProcessor extends BaseContainerBlockInteractedProcessor {

    @Inject
    private NetworkManager networkManager;

    @Override
    public boolean interactive(Player player, Block targetBlock, Vector3 targetPosition) {
        this.inventoryManager.setOpenContainerStatus(player, ContainerType.BIG_WORKBENCH);
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setContainerId(InventoryConstants.CONTAINER_ID_NONE);
        containerOpenPacket.setContainType(InventoryConstants.CONTAINER_TYPE_WORKBENCH);
        containerOpenPacket.setX(targetPosition.getX());
        containerOpenPacket.setY(targetPosition.getY());
        containerOpenPacket.setZ(targetPosition.getZ());
        containerOpenPacket.setTargetEntityId(player.getRuntimeId());
        networkManager.sendMessage(player.getClientAddress(), containerOpenPacket);

        return true;
    }
}
