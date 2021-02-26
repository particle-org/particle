package com.particle.model.inventory;

import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.player.Player;

public class PlayerInventory extends Inventory {

    private int hotBarSize = 9;

    /**
     * 玩家背包中手握的index
     */
    private int itemInHandle;

    /**
     * 玩家背包的热门index，指的是背包快捷栏
     */
    private int[] hotBar;

    public PlayerInventory() {
        this.setContainerType(ContainerType.PLAYER);
        this.setSize(ContainerType.PLAYER.getDefaultSize());
        this.hotBar = new int[hotBarSize];
        for (int i = 0; i < hotBarSize; i++) {
            hotBar[i] = i;
        }
    }

    public int getItemInHandle() {
        return itemInHandle;
    }

    public void setItemInHandle(int itemInHandle) {
        this.itemInHandle = itemInHandle;
    }

    public int[] getHotBar() {
        return hotBar;
    }

    public int getHotBarSize() {
        return hotBarSize;
    }

    public void setHotBarSize(int hotBarSize) {
        this.hotBarSize = hotBarSize;
    }

    /**
     * 判断是否是hotbar槽
     *
     * @param slot
     * @return
     */
    public boolean isHotBarSlot(int slot) {
        return slot >= 0 && slot <= hotBarSize;
    }

    @Override
    public void setInventoryHolder(InventoryHolder inventoryHolder) {
        super.setInventoryHolder(inventoryHolder);
        if (inventoryHolder instanceof Player) {
            this.addView((Player) inventoryHolder);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
