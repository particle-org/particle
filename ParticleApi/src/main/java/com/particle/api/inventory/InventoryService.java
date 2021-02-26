package com.particle.api.inventory;

import com.particle.model.entity.Entity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.player.Player;

import java.util.Collection;

public interface InventoryService {

    /**
     * 获取当前打开容器状态
     *
     * @param entity 生物
     */
    void setOpenContainerStatus(Entity entity, ContainerType status);

    /**
     * 获取当前打开容器状态
     *
     * @param entity 生物
     * @return 返回状态，默认是打开玩家背包
     */
    ContainerType getOpenContainerStatus(Entity entity);

    /**
     * 获取玩家所属的背包列表，包括玩家背包、副手、装备、合成台、铁砧、单格、末影箱、附魔台
     * 不包括观察者列表
     *
     * @param entity 生物
     * @return 背包列表
     */
    Collection<Inventory> getAllOwnedInventory(Entity entity);

    /**
     * 根据containerId获取玩家对应的inventory
     *
     * @param entity      生物
     * @param containerId 打开背包的索引ID，详见 {@link InventoryConstants}
     * @return 背包列表
     */
    Inventory getInventoryByContainerId(Entity entity, int containerId);

    /**
     * 根据 tileEntity 获取对应的inventory
     *
     * @param tileEntity
     * @return 背包列表
     */
    Inventory getInventoryByTileEntity(TileEntity tileEntity);

    /**
     * 玩家添加某个背包（随机containerId）
     * 针对 multi_inventory
     *
     * @param entity    玩家
     * @param inventory 打开背包的索引ID，详见 {@link InventoryConstants}
     */
    boolean bindMultiInventory(Entity entity, Inventory inventory);

    /**
     * 玩家删除某个背包
     * 针对 multi_inventory
     *
     * @param entity      玩家
     * @param containerId 打开背包的索引ID，详见 {@link InventoryConstants}
     */
    void unbindMultiInventory(Entity entity, int containerId);

    /**
     * 根据给定的inventory获取玩家打开背包的对应containerId,
     * 详见 {@link InventoryConstants}
     *
     * @param entity    玩家
     * @param inventory 背包
     * @return 如果未找到，返回-1
     */
    int getMapIdFromMultiOwned(Entity entity, Inventory inventory);

    /**
     * 清理玩家默认背包
     *
     * @param player 玩家
     */
    void clearPlayerInventory(Player player);

    /**
     * 通知客户端，关闭当前的背包UI
     *
     * @param player 玩家
     */
    void notifyClientsAllInventoryUiClosed(Player player);

    /**
     * 强制通知客户端，玩家所属的背包数据
     *
     * @param player 玩家
     */
    void notifyPlayerAllInventoryChanged(Player player);

    /**
     * 通知玩家，创造模式下的物品列表
     *
     * @param player 玩家
     */
    void notifyPlayerWithCreativeContents(Player player);

    /**
     * 通知玩家，观察者模式下的物品列表
     *
     * @param player 玩家
     */
    void notifyPlayerWithObserverContents(Player player);

    /**
     * 打开entity的附属多背包
     * 如给NPC附属了玩家背包等资料
     *
     * @param entity
     */
    void openEntityMultiInventory(Entity entity);
}
