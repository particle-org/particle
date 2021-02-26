package com.particle.model.inventory.recipe;

import com.particle.model.item.ItemStack;

public class BrewingRecipe extends Recipe {

    public BrewingRecipe() {
        this.setType(6);
    }

    private ItemStack potion;

    private ItemStack input;

    public ItemStack getPotion() {
        return potion;
    }

    public void setPotion(ItemStack potion) {
        this.potion = potion;
    }

    public ItemStack getInput() {
        return input;
    }

    public void setInput(ItemStack input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "BrewingRecipe{" +
                "potion=" + potion +
                ", input=" + input +
                ", output=" + getOutput() +
                '}';
    }
}
