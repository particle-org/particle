package com.particle.game.ui;

import com.particle.api.ui.IEnderBagServiceApi;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.holder.BlockInventoryHolder;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.PlayerEnderChestInventory;
import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.NetworkService;
import com.particle.model.network.packets.data.ContainerOpenPacket;
import com.particle.model.network.packets.data.UpdateBlockPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class EnderBagService implements IEnderBagServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnderBagService.class);

    @Inject
    protected InventoryAPIProxy inventoryServiceProxy;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private PositionService positionService;

    @Inject
    private LevelService levelService;

    @Inject
    private NetworkService networkService;

    /**
     * 玩家打开箱子的缓存
     */
    private Map<String, BlockRecorder> openingCache = new HashMap<>();

    @Override
    public void open(Player player) {
        // 获取箱子
        Inventory playerEnderChestInventory = this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ENDER);
        if (!(playerEnderChestInventory instanceof PlayerEnderChestInventory)) {
            LOGGER.error("EnderChest not found from {}!", player.getIdentifiedStr());
            return;
        }

        // 记录刷新箱子位置的方块
        Vector3 operatePosition = new Vector3(positionService.getPosition(player)).add(0, 2, 0);
        Block operateBlock = this.levelService.getBlockAt(player.getLevel(), operatePosition);

        // 缓存位置
        BlockRecorder blockRecorder = new BlockRecorder();
        blockRecorder.block = operateBlock;
        blockRecorder.positin = operatePosition;
        this.openingCache.put(player.getIdentifiedStr(), blockRecorder);

        // 刷新一个假箱子
        Block block = Block.getBlock(BlockPrototype.CHEST);
        UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
        updateBlockPacket.setBlock(block);
        updateBlockPacket.setVector3(operatePosition);
        updateBlockPacket.setRuntimeId(block.getRuntimeId());
        updateBlockPacket.setFlag(UpdateBlockPacket.All);
        updateBlockPacket.setLayer(UpdateBlockPacket.DATA_LAYER_NORMAL);
        this.networkService.sendMessage(player.getClientAddress(), updateBlockPacket);

        // 打开箱子
        playerEnderChestInventory.setInventoryHolder(new BlockInventoryHolder(playerEnderChestInventory, new Vector3f(operatePosition)));
        this.inventoryManager.setOpenContainerStatus(player, ContainerType.ENDER_CHEST);
        playerEnderChestInventory.addView(player);

        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setContainerId(this.inventoryManager.getMapIdFromMultiOwned(player, playerEnderChestInventory));
        containerOpenPacket.setContainType(playerEnderChestInventory.getContainerType().getType());
        InventoryHolder holder = playerEnderChestInventory.getInventoryHolder();

        containerOpenPacket.setX(holder.getPosition().getFloorX());
        containerOpenPacket.setY(holder.getPosition().getFloorY());
        containerOpenPacket.setZ(holder.getPosition().getFloorZ());
        this.networkService.sendMessage(player.getClientAddress(), containerOpenPacket);

        this.inventoryServiceProxy.notifyPlayerContentChanged(playerEnderChestInventory);
    }

    public boolean onCloseHandleIfPlayerOpened(Player player) {
        BlockRecorder blockRecorder = this.openingCache.remove(player.getIdentifiedStr());
        if (blockRecorder != null) {
            // 修复对应位置的方块
            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
            updateBlockPacket.setBlock(blockRecorder.block);
            updateBlockPacket.setVector3(blockRecorder.positin);
            updateBlockPacket.setRuntimeId(blockRecorder.block.getRuntimeId());
            updateBlockPacket.setFlag(blockRecorder.block.getMeta());
            updateBlockPacket.setLayer(UpdateBlockPacket.DATA_LAYER_NORMAL);
            this.networkService.sendMessage(player.getClientAddress(), updateBlockPacket);

            return true;
        } else {
            return false;
        }
    }

    private static class BlockRecorder {
        private Block block;
        private Vector3 positin;
    }
}
