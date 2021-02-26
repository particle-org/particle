package com.particle.model.inventory;

import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public abstract class Inventory {

    public static final int DEFAULT_MAX_STACK_SIZE = 64;

    /**
     * 背包类型
     */
    private ContainerType containerType;

    /**
     * 背包的总共槽数
     */
    private int size;

    /**
     * 背包每个槽最大可放物品数
     */
    private int maxStackSize = DEFAULT_MAX_STACK_SIZE;

    /**
     * 背包名称
     */
    private String name;

    /**
     * 背包标题
     */
    private String title;

    /**
     * 背包里所有的槽集合
     */
    private Map<Integer, ItemStack> allSlots = new ConcurrentHashMap<>();

    /**
     * 箱子状态，在箱子所在区块卸载时标记不可用
     */
    private boolean isActive = true;

    /**
     * 背包的可见者，只能是玩家
     */
    private Set<Player> viewers = new ConcurrentSkipListSet<Player>(new Comparator<Player>() {
        @Override
        public int compare(Player o1, Player o2) {
            if (o1 == null) {
                return -1;
            } else if (o2 == null) {
                return 1;
            } else if (o1.getRuntimeId() < o2.getRuntimeId()) {
                return -1;
            } else if (o1.getRuntimeId() > o2.getRuntimeId()) {
                return 1;
            } else {
                return 0;
            }
        }
    });

    /**
     * 背包的属有者
     */
    private InventoryHolder inventoryHolder;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<Integer, ItemStack> getAllSlots() {
        return allSlots;
    }

    public void setAllSlots(Map<Integer, ItemStack> allSlots) {
        this.allSlots = allSlots;
    }

    /**
     * 更新具体某个槽
     *
     * @param index
     * @param itemStack
     */
    public void putSlot(int index, ItemStack itemStack) {
        allSlots.put(index, itemStack);
    }

    public Set<Player> getViewers() {
        return viewers;
    }

    public void setViewers(Set<Player> viewers) {
        this.viewers = viewers;
    }

    /**
     * 添加观察
     *
     * @param player
     */
    public void addView(Player player) {
        if (viewers.contains(player)) {
            return;
        }
        viewers.add(player);
    }

    /**
     * 删除观察
     *
     * @param player
     */
    public void removeView(Player player) {
        if (viewers.contains(player)) {
            viewers.remove(player);
        }
    }

    public InventoryHolder getInventoryHolder() {
        return inventoryHolder;
    }

    public void setInventoryHolder(InventoryHolder inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
    }

    public ContainerType getContainerType() {
        return containerType;
    }

    public void setContainerType(ContainerType containerType) {
        this.containerType = containerType;
        this.setName(containerType.getDefaultTitle());
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        String itemInfo = "";
        for (Map.Entry<Integer, ItemStack> entry : this.allSlots.entrySet()) {
            itemInfo = itemInfo + "slot=" + entry.getKey() + ", value=" + entry.getValue().toString() + "\n";
        }
        String viewInfo = "";
        for (Player player : viewers) {
            viewInfo += player.getIdentifiedStr();
            viewInfo += "\n";
        }
        return "Inventory{" +
                "containerType=" + containerType.getDefaultTitle() +
                ", allSlots=" + itemInfo +
                ", viewInfo=" + viewInfo +
                '}';
    }
}
