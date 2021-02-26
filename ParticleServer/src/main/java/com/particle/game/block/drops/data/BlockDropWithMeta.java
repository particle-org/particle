package com.particle.game.block.drops.data;

import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;

import java.util.ArrayList;
import java.util.List;

public class BlockDropWithMeta implements IBlockDrop {

    private ItemPrototype itemPrototype;
    private int amount;
    private int meta = -1;

    public BlockDropWithMeta(ItemPrototype itemPrototype, int amount, int meta) {
        this.itemPrototype = itemPrototype;
        this.amount = amount;
        this.meta = meta;
    }

    public BlockDropWithMeta(ItemPrototype itemPrototype, int amount) {
        this.itemPrototype = itemPrototype;
        this.amount = amount;
    }

    @Override
    public List<ItemStack> getDrops(int pmeta) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>(1);
        ItemStack itemStack = ItemStack.getItem(itemPrototype, amount);
        if (pmeta != -1) {
            itemStack.setMeta(pmeta);
        }
        itemStacks.add(itemStack);
        return itemStacks;
    }
}
