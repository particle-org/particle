package com.particle.game.player.craft.components;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class CraftModule extends BehaviorModule {

    private ItemStack[] craftInputs = new ItemStack[9];
    private ItemStack output = null;
    private Set<ItemStack> externalOutput = new HashSet<>();

    /**
     * 清除合成台物品
     */
    public void clearInputs() {
        this.craftInputs = new ItemStack[9];
    }

    public void clearOutputs() {
        this.output = null;
        this.externalOutput.clear();
    }

    /**
     * 增加合成台物品
     *
     * @param item
     */
    public void addCraftInputs(ItemStack item, int slot) {
        if (slot < 0 || slot > 8) {
            return;
        }
        // 输入可能多次分多个包传递
        ItemStack oldItem = this.craftInputs[slot];
        if (oldItem == null) {
            this.craftInputs[slot] = item;
        } else if (!oldItem.equalsAll(item)) {
            this.craftInputs[slot] = item;
        } else {
            item.setCount(item.getCount() + oldItem.getCount());
            this.craftInputs[slot] = item;
        }
    }

    /**
     * 获取输入内容
     *
     * @return
     */
    public ItemStack[] getCraftInputs() {
        return this.craftInputs;
    }

    /**
     * 增加输出内容
     *
     * @param itemStacks
     */
    public void setCraftOutputs(ItemStack itemStacks) {
        this.output = itemStacks;
    }

    /**
     * 增加输出内容
     *
     * @param itemStacks
     */
    public void addExternalCraftOutputs(ItemStack itemStacks) {
        this.externalOutput.add(itemStacks);
    }

    public ItemStack getCraftOutput() {
        return output;
    }

    public Set<ItemStack> getExternalOutput() {
        return externalOutput;
    }
}
