package com.particle.model.inventory;

import com.particle.model.inventory.common.ContainerType;
import com.particle.model.item.enchantment.Enchantment;

public class EnchantInventory extends Inventory {

    /**
     * 附魔的目标
     */
    public static final int TARGET = 0;

    /**
     * 青金石的位置
     */
    public static final int SACRIFICE = 1;

    /**
     * 结果
     */
    public static final int RESULT = 2;

    private int[] levels = null;

    private int bookshelfAmount = 0;

    private Enchantment[] enchantments;

    public EnchantInventory() {
        this.setContainerType(ContainerType.ENCHANT_TABLE);
        this.setSize(ContainerType.ENCHANT_TABLE.getDefaultSize());
    }

    public int[] getLevels() {
        return levels;
    }

    public void setLevels(int[] levels) {
        this.levels = levels;
    }

    public int getBookshelfAmount() {
        return bookshelfAmount;
    }

    public void setBookshelfAmount(int bookshelfAmount) {
        this.bookshelfAmount = bookshelfAmount;
    }

    public Enchantment[] getEnchantments() {
        return enchantments;
    }

    public void setEnchantments(Enchantment[] enchantments) {
        this.enchantments = enchantments;
    }
}
