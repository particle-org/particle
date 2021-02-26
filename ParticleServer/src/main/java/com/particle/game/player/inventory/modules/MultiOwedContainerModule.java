package com.particle.game.player.inventory.modules;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.InventoryConstants;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiOwedContainerModule extends BehaviorModule {

    /**
     * entity所属的背包
     */
    private Map<Integer, Inventory> ownInventories = new ConcurrentHashMap<>();

    /**
     * 允许的containerId
     * 其中合成台、铁砧、附魔台是玩家私人所属，关闭后会回到背包
     */
    private static final List<Integer> allowContainerIds = Arrays.asList(
            InventoryConstants.CONTAINER_ID_PLAYER,
            InventoryConstants.CONTAINER_ID_ARMOR,
            InventoryConstants.CONTAINER_ID_OFFHAND,
            InventoryConstants.CONTAINER_ID_PLAYER_UI,
            InventoryConstants.CONTAINER_ID_WORKBENCH,
            InventoryConstants.CONTAINER_ID_ENDER,
            InventoryConstants.CONTAINER_ID_ANVIL,
            InventoryConstants.CONTAINER_ID_ENCHANT);

    /**
     * 获取所属的背包列表
     *
     * @return
     */
    public Collection<Inventory> values() {
        return this.ownInventories.values();
    }

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
    public static boolean checkContainerIdValid(int containerId) {
        if (allowContainerIds.contains(containerId)) {
            return true;
        }
        return false;
    }

    /**
     * 添加某个背包（固定containerId）
     *
     * @param containerId
     * @param inventory
     */
    public boolean addInventory(int containerId, Inventory inventory) {
        if (!this.checkContainerIdValid(containerId)) {
            throw new ProphetException(ErrorCode.CORE_EROOR, "containerId不合法");
        }
        if (this.ownInventories.containsKey(containerId)) {
            return false;
        }
        this.ownInventories.put(containerId, inventory);
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
