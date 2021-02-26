package com.particle.game.player.inventory.service.impl;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.player.inventory.modules.ContainerViewerModule;
import com.particle.game.sound.SoundService;
import com.particle.game.world.animation.InventoryAnimationService;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.math.Vector3;
import com.particle.model.network.packets.data.ContainerOpenPacket;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundType;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 末影箱
 */
@Singleton
public class EnderChestInventoryAPI extends CommonInventoryAPI {

    private static final ECSModuleHandler<ContainerViewerModule> CONTAINER_VIEWER_MODULE_HANDLER = ECSModuleHandler.buildHandler(ContainerViewerModule.class);

    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private InventoryAnimationService inventoryAnimationService;

    @Inject
    private SoundService soundService;


    @Override
    public void addView(Player player, Inventory inventory) {
        super.addView(player, inventory);
        this.openEnder(player, inventory);
    }

    @Override
    public void onClose(Player player, Inventory inventory) {
        this.closeEnder(player, inventory);
        super.onClose(player, inventory);
    }

    /**
     * 打开末影箱
     *
     * @param player
     * @param inventory
     */
    private void openEnder(Player player, Inventory inventory) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setContainerId(this.inventoryManager.getMapIdFromMultiOwned(player, inventory));
        containerOpenPacket.setContainType(inventory.getContainerType().getType());
        InventoryHolder holder = inventory.getInventoryHolder();

        containerOpenPacket.setX(holder.getPosition().getFloorX());
        containerOpenPacket.setY(holder.getPosition().getFloorY());
        containerOpenPacket.setZ(holder.getPosition().getFloorZ());
        networkManager.sendMessage(player.getClientAddress(), containerOpenPacket);
        this.notifyPlayerContentChanged(inventory);

        Vector3 position = new Vector3(holder.getPosition());
        TileEntity entityAt = tileEntityService.getEntityAt(player.getLevel(), position);

        ContainerViewerModule containerViewerModule = CONTAINER_VIEWER_MODULE_HANDLER.getModule(entityAt);
        int size = 0;
        if (containerViewerModule != null) {
            containerViewerModule.addPlayer(player);
            size = containerViewerModule.getViewerSize();
        }
        if (size == 1) {
            this.inventoryAnimationService.sendOpenInventoryPacket(player.getLevel(), position);
            this.soundService.broadcastPlaySound(player.getLevel(), SoundType.RANDOM_CHESTOPEN, position);
        }
    }

    /**
     * 关闭末影箱
     */
    private void closeEnder(Player player, Inventory inventory) {
        InventoryHolder holder = inventory.getInventoryHolder();
        Vector3 position = new Vector3(holder.getPosition());
        TileEntity entityAt = tileEntityService.getEntityAt(player.getLevel(), position);

        // 拦截虚拟箱子时空指针问题
        if (entityAt == null) {
            inventory.setInventoryHolder(null);
            return;
        }

        int size = 0;
        ContainerViewerModule containerViewerModule = CONTAINER_VIEWER_MODULE_HANDLER.getModule(entityAt);
        if (containerViewerModule != null) {
            containerViewerModule.removePlayer(player);
            size = containerViewerModule.getViewerSize();
        }
        if (size == 0) {
            this.inventoryAnimationService.sendCloseInventoryPacket(player.getLevel(), position);
            this.soundService.broadcastPlaySound(player.getLevel(), SoundType.RANDOM_CHESTCLOSED, position);
        }
        inventory.setInventoryHolder(null);
    }
}
