package com.particle.model.inventory;

import com.particle.model.inventory.common.ContainerType;
import com.particle.model.item.ItemStack;

public class AnvilInventory extends Inventory {

    /**
     * 需要修复的物品位置
     */
    public static final int TARGET = 0;

    /**
     * 燃料位置
     */
    public static final int SACRIFICE = 1;

    /**
     * 结果
     */
    public static final int RESULT = 2;

    public AnvilInventory() {
        this.setContainerType(ContainerType.ANVIL);
        this.setSize(ContainerType.ANVIL.getDefaultSize());
    }

    /**
     * 更新具体某个槽
     *
     * @param index
     * @param itemStack
     */
    @Override
    public void putSlot(int index, ItemStack itemStack) {
        super.putSlot(index, itemStack);
    }
}
