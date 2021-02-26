package com.particle.model.inventory;

import com.particle.model.inventory.common.ContainerType;
import com.particle.model.item.ItemStack;

public class FurnaceInventory extends Inventory {

    /**
     * 冶炼物
     */
    public static final int SmeltingIndex = 0;

    /**
     * 燃料
     */
    public static final int FuelIndex = 1;

    /**
     * 结果
     */
    public static final int ResultIndex = 2;

    public FurnaceInventory() {
        this.setContainerType(ContainerType.FURNACE);
        this.setSize(ContainerType.FURNACE.getDefaultSize());
    }

    /**
     * 获取冶炼物
     *
     * @return
     */
    public ItemStack getSmelt() {
        return getAllSlots().get(SmeltingIndex);
    }

    /**
     * 获取燃料
     *
     * @return
     */
    public ItemStack getFuel() {
        return getAllSlots().get(FuelIndex);
    }

    /**
     * 获取结果
     *
     * @return
     */
    public ItemStack getResult() {
        return getAllSlots().get(ResultIndex);
    }

    /**
     * 设置smelt
     *
     * @param smelt
     */
    public void setSmelt(ItemStack smelt) {
        getAllSlots().put(SmeltingIndex, smelt);
    }

    /**
     * 设置燃料
     *
     * @param fuel
     */
    public void setFuel(ItemStack fuel) {
        getAllSlots().put(FuelIndex, fuel);
    }

    /**
     * 设置结果
     *
     * @param result
     */
    public void setResult(ItemStack result) {
        getAllSlots().put(ResultIndex, result);
    }
}
