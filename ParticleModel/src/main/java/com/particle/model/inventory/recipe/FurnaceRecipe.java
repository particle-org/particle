package com.particle.model.inventory.recipe;

import com.particle.model.inventory.common.RecipeType;
import com.particle.model.item.ItemStack;

public class FurnaceRecipe extends Recipe {

    public FurnaceRecipe() {
        this.setType(RecipeType.FurnaceAuxRecipe);
    }

    private ItemStack input;

    public ItemStack getInput() {
        return input;
    }

    public void setInput(ItemStack input) {
        this.input = input;
    }
}
