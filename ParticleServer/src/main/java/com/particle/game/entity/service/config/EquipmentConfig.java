package com.particle.game.entity.service.config;

import com.particle.model.item.ItemStack;

public class EquipmentConfig {
    private String position;
    private float probability;
    private ItemStack itemStack;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
