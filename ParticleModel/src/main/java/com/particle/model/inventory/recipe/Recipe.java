package com.particle.model.inventory.recipe;

import com.particle.model.item.ItemStack;

public abstract class Recipe {

    private int type;

    /**
     * 合成配方输出
     */
    private ItemStack output;

    private String tag = "crafting_table";

    private String furnaceTag = "furnace";

    public ItemStack getOutput() {
        return output;
    }

    public void setOutput(ItemStack output) {
        this.output = output;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFurnaceTag() {
        return furnaceTag;
    }

    public void setFurnaceTag(String furnaceTag) {
        this.furnaceTag = furnaceTag;
    }
}
