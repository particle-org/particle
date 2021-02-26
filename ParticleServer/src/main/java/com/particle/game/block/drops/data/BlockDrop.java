package com.particle.game.block.drops.data;

import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;

import java.util.ArrayList;
import java.util.List;

public class BlockDrop implements IBlockDrop {
    private ItemPrototype itemPrototype;
    private int amount;
    private int meta = -1;

    public BlockDrop(ItemPrototype itemPrototype, int amount, int meta) {
        this.itemPrototype = itemPrototype;
        this.amount = amount;
        this.meta = meta;
    }

    public BlockDrop(ItemPrototype itemPrototype, int amount) {
        this.itemPrototype = itemPrototype;
        this.amount = amount;
    }

    @Override
    public List<ItemStack> getDrops(int meta) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>(1);
        ItemStack itemStack = ItemStack.getItem(itemPrototype, amount);
        if (meta != -1) {
            itemStack.setMeta(meta);
        }
        itemStacks.add(itemStack);
        return itemStacks;
    }

}
