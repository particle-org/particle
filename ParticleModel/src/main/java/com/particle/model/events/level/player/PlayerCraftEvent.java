package com.particle.model.events.level.player;

import com.particle.model.inventory.recipe.Recipe;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

import java.util.List;

public class PlayerCraftEvent extends LevelPlayerEvent {

    private Recipe recipe;
    private List<ItemStack> inputList;
    private ItemStack output;

    public PlayerCraftEvent(Player player, Recipe recipe, List<ItemStack> inputList, ItemStack output) {
        super(player);
        this.recipe = recipe;
        this.inputList = inputList;
        this.output = output;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public List<ItemStack> getInputList() {
        return inputList;
    }

    public ItemStack getOutput() {
        return output;
    }
}
