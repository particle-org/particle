package com.particle.model.inventory;

import com.particle.model.inventory.common.ContainerType;

public class ArmorInventory extends Inventory {

    /**
     * 头盔
     */
    public static final int HELMET = 0;

    /**
     * 胸甲
     */
    public static final int CHESTPLATE = 1;

    /**
     * 绑腿
     */
    public static final int LEGGINGS = 2;

    /**
     * 鞋子
     */
    public static final int BOOTS = 3;


    public ArmorInventory() {
        this.setContainerType(ContainerType.ARMOR);
        this.setSize(ContainerType.ARMOR.getDefaultSize());
    }
}
