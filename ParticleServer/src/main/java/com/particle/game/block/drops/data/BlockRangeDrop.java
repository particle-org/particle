package com.particle.game.block.drops.data;

import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;

import java.util.ArrayList;
import java.util.List;

public class BlockRangeDrop implements IBlockDrop {
    private ItemPrototype itemPrototype;
    private int meta = -1;
    private int minAmount;
    private int maxAmount;

    public BlockRangeDrop(ItemPrototype itemPrototype, int meta, int minAmount, int maxAmount) {
        this.itemPrototype = itemPrototype;
        this.meta = meta;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public BlockRangeDrop(ItemPrototype itemPrototype, int minAmount, int maxAmount) {
        this.itemPrototype = itemPrototype;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    public List<ItemStack> getDrops(int pmeta) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>(1);
        int amount = (int) (Math.random() * (maxAmount - minAmount)) + minAmount;
        ItemStack itemStack = null;
        if (amount > 0) {
            itemStack = ItemStack.getItem(itemPrototype, amount);
        }

        if (itemStack != null) {
            if (meta != -1) {
                itemStack.setMeta(meta);
            }
            itemStacks.add(itemStack);
        }

        return itemStacks;
    }

}
