package com.particle.game.ui;

import com.particle.api.ui.IChestSelectorServiceApi;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.math.Vector3;
import com.particle.model.network.packets.data.ContainerClosePacket;
import com.particle.model.network.packets.data.ContainerOpenPacket;
import com.particle.model.network.packets.data.UpdateBlockPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ChestSelectorService implements IChestSelectorServiceApi {

    @Inject
    private LevelService levelService;

    @Inject
    private PositionService positionService;

    @Inject
    private NetworkManager networkService;

    @Inject
    private InventoryManager inventoryService;
    @Inject
    private InventoryAPIProxy inventoryAPI;

    /**
     * 玩家打开箱子的缓存
     */
    private Map<String, BlockRecorder> openingCache = new HashMap<>();

    public static final int CONTAINER_ID = 101;

    @Override
    public void open(Player player, IChestSelectorCallback callback) {
        Vector3 operatePosition = new Vector3(positionService.getPosition(player)).add(0, 2, 0);

        // 记录刷新箱子位置的方块
        Block operateBlock = this.levelService.getBlockAt(player.getLevel(), operatePosition);

        // 缓存位置
        BlockRecorder blockRecorder = new BlockRecorder();
        blockRecorder.block = operateBlock;
        blockRecorder.positin = operatePosition;
        blockRecorder.callback = callback;
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

        // 打开GUI
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setContainerId(CONTAINER_ID);
        containerOpenPacket.setContainType(ContainerType.CHEST.getType());

        containerOpenPacket.setX(operatePosition.getX());
        containerOpenPacket.setY(operatePosition.getY());
        containerOpenPacket.setZ(operatePosition.getZ());
        this.networkService.sendMessage(player.getClientAddress(), containerOpenPacket);
    }

    /**
     * 箱子操作回调
     *
     * @param player
     * @param inventoryActionData
     * @return 是否处理了该玩家的回调
     */
    public boolean onSelectItem(Player player, InventoryActionData[] inventoryActionData) {
        // 查询缓存
        BlockRecorder blockRecorder = openingCache.get(player.getIdentifiedStr());

        if (blockRecorder != null) {
            for (InventoryActionData inventoryActionDatum : inventoryActionData) {
                // 检查是否有对假背包的操作
                if (inventoryActionDatum.getSource().getContainerId() == CONTAINER_ID) {
                    this.onCloseHandleIfPlayerOpened(player);

                    Inventory inventory = this.inventoryService.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
                    this.inventoryAPI.notifyPlayerContentChanged(inventory);

                    // 1.16 要等待關箱包發出
                    player.getLevel().getLevelSchedule().scheduleDelayTask("delay_handle", () -> {
                        // 执行回调
                        blockRecorder.callback.handle(player, inventoryActionDatum.getToItem());
                    }, 100);
                }
            }

            return true;
        }

        return false;
    }

    public void close(Player player) {
        ContainerClosePacket containerClosePacket = new ContainerClosePacket();
        containerClosePacket.setContainerId(CONTAINER_ID);
        this.networkService.sendMessage(player.getClientAddress(), containerClosePacket);
    }

    public void onClose(Player player) {
        // 查询缓存
        BlockRecorder blockRecorder = openingCache.remove(player.getIdentifiedStr());

        if (blockRecorder != null) {
            // 修复对应位置的方块
            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
            updateBlockPacket.setBlock(blockRecorder.block);
            updateBlockPacket.setVector3(blockRecorder.positin);
            updateBlockPacket.setRuntimeId(blockRecorder.block.getRuntimeId());
            updateBlockPacket.setFlag(blockRecorder.block.getMeta());
            updateBlockPacket.setLayer(UpdateBlockPacket.DATA_LAYER_NORMAL);
            this.networkService.sendMessage(player.getClientAddress(), updateBlockPacket);
        }
    }

    public boolean onCloseHandleIfPlayerOpened(Player player) {
        // 查询缓存
        BlockRecorder blockRecorder = openingCache.remove(player.getIdentifiedStr());

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

    private class BlockRecorder {
        private Block block;
        private Vector3 positin;
        private IChestSelectorCallback callback;
    }
}
