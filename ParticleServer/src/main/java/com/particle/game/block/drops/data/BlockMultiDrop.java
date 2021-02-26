package com.particle.game.block.drops.data;

import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;

import java.util.ArrayList;
import java.util.List;

public class BlockMultiDrop implements IBlockDrop {
    private ItemPrototype itemPrototype1;
    private int amount1;
    private int meta1 = -1;
    private ItemPrototype itemPrototype2;
    private int amount2;
    private int meta2 = -1;

    public BlockMultiDrop(ItemPrototype itemPrototype1, int amount1, int meta1, ItemPrototype itemPrototype2, int amount2, int meta2) {
        this.itemPrototype1 = itemPrototype1;
        this.amount1 = amount1;
        this.meta1 = meta1;
        this.itemPrototype2 = itemPrototype2;
        this.amount2 = amount2;
        this.meta2 = meta2;
    }

    public BlockMultiDrop(ItemPrototype itemPrototype1, int amount1, ItemPrototype itemPrototype2, int amount2) {
        this.itemPrototype1 = itemPrototype1;
        this.amount1 = amount1;
        this.itemPrototype2 = itemPrototype2;
        this.amount2 = amount2;
    }


    @Override
    public List<ItemStack> getDrops(int pmeta) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>(2);
        ItemStack itemStack = ItemStack.getItem(itemPrototype1, amount1);
        if (meta1 != -1) {
            itemStack.setMeta(meta1);
        }
        itemStacks.add(itemStack);
        itemStack = ItemStack.getItem(itemPrototype2, amount2);
        if (meta2 != -1) {
            itemStack.setMeta(meta2);
        }
        itemStacks.add(itemStack);

        return itemStacks;
    }
}
