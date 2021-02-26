package com.particle.game.player.inventory.modules;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.InventoryConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiObservedContainerModule extends BehaviorModule {

    /**
     * entity所属的背包
     */
    private Map<Integer, Inventory> ownInventories = new ConcurrentHashMap<>();

    /**
     * 记录当前玩家使用id索引
     */
    private int currentContainerId = InventoryConstants.CONTAINER_ID_FIRST;

    /**
     * 根据containerId获取inventory
     *
     * @param containerId
     * @return
     */
    public Inventory getInventoryByContainerId(int containerId) {
        return this.ownInventories.get(containerId);
    }

    /**
     * 检测containerId是否合法
     *
     * @param containerId
     * @return
     */
    public boolean checkContainerIdValid(int containerId) {
        if (containerId < InventoryConstants.CONTAINER_ID_FIRST || containerId >= InventoryConstants.CONTAINER_ID_LAST) {
            return false;
        }
        return true;
    }

    /**
     * 添加某个背包（固定containerId）
     *
     * @param containerId
     * @param inventory
     */
    public boolean addInventory(int containerId, Inventory inventory) {
        if (containerId < InventoryConstants.CONTAINER_ID_FIRST || containerId >= InventoryConstants.CONTAINER_ID_LAST) {
            throw new ProphetException(ErrorCode.CORE_EROOR, "containerId不合法");
        }
        if (this.ownInventories.containsKey(containerId)) {
            return false;
        }
        this.ownInventories.put(containerId, inventory);
        return true;
    }

    /**
     * 添加某个背包（随机containerId）
     *
     * @param inventory
     */
    public boolean addInventory(Inventory inventory) {
        if (this.ownInventories.size() >= InventoryConstants.CONTAINER_ID_LAST) {
            throw new ProphetException(ErrorCode.CORE_EROOR, "该entity无法附带100个背包");
        }
        this.currentContainerId = ++this.currentContainerId % InventoryConstants.CONTAINER_ID_LAST;
        if (this.currentContainerId == 0) {
            this.currentContainerId = InventoryConstants.CONTAINER_ID_FIRST;
        }
        if (this.ownInventories.containsKey(currentContainerId)) {
            return this.addInventory(inventory);
        }
        this.ownInventories.put(this.currentContainerId, inventory);
        return true;
    }

    /**
     * 删除某个背包
     *
     * @param containerId
     */
    public void removeInventory(int containerId) {
        if (this.ownInventories.containsKey(containerId)) {
            this.ownInventories.remove(containerId);
        }
    }

    /**
     * 根据给定的inventory获取其containerId
     * 如果未找到，返回-1
     *
     * @param inventory
     * @return
     */
    public int getMapIdFromMultiOwned(Inventory inventory) {
        for (Map.Entry<Integer, Inventory> inventoryEntry : this.ownInventories.entrySet()) {
            if (inventoryEntry.getValue().equals(inventory)) {
                return inventoryEntry.getKey();
            }
        }
        return -1;
    }

}
