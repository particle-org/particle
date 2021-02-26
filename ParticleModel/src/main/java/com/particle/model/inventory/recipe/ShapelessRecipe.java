package com.particle.model.inventory.recipe;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.inventory.common.RecipeType;
import com.particle.model.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShapelessRecipe extends Recipe {

    public ShapelessRecipe() {
        this.setType(RecipeType.ShapelessRecipe);
    }

    /**
     * 该配方的唯一性标志
     */
    private UUID uuid;

    /**
     * 输入
     */
    private List<ItemStack> inputs = new ArrayList<>();

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public void addInputs(ItemStack input) {
        if (this.inputs.size() > 9) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "配方错误");
        }
        ItemStack newInput = input.clone();
        newInput.setCount(1);
        while (input.getCount() > 0) {
            this.inputs.add(newInput.clone());
            input.setCount(input.getCount() - 1);
        }
    }

    /**
     * 输入的数量
     *
     * @return
     */
    public int getInputCounts() {
        return this.inputs.size();
    }
}
