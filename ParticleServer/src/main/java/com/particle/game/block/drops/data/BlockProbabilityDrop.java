package com.particle.game.block.drops.data;

import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;

import java.util.ArrayList;
import java.util.List;

public class BlockProbabilityDrop implements IBlockDrop {
    private ItemPrototype itemPrototype;
    private double probability;
    private int meta = -1;

    public BlockProbabilityDrop(ItemPrototype itemPrototype, double probability, int meta) {
        this.itemPrototype = itemPrototype;
        this.probability = probability;
        this.meta = meta;
    }

    public BlockProbabilityDrop(ItemPrototype itemPrototype, double probability) {
        this.itemPrototype = itemPrototype;
        this.probability = probability;
    }


    @Override
    public List<ItemStack> getDrops(int pmeta) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>(1);
        if (Math.random() < probability) {
            ItemStack itemStack = ItemStack.getItem(itemPrototype, 1);
            if (meta != -1) {
                itemStack.setMeta(meta);
            } else {
                itemStack.setMeta(pmeta);
            }
            itemStacks.add(itemStack);
        }

        return itemStacks;
    }
}
