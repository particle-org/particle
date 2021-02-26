package com.particle.model.network.packets.data;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.Arrays;
import java.util.UUID;

public class CraftingEventPacket extends DataPacket {

    @Override
    public int pid() {
        return ProtocolInfo.CRAFTING_EVENT_PACKET;
    }

    private int containerId;

    private int type;

    private UUID recipeId;

    private ItemStack[] inputs;

    private ItemStack[] outputs;

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UUID getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(UUID recipeId) {
        this.recipeId = recipeId;
    }

    public ItemStack[] getInputs() {
        return inputs;
    }

    public void setInputs(ItemStack[] inputs) {
        this.inputs = inputs;
    }

    public ItemStack[] getOutputs() {
        return outputs;
    }

    public void setOutputs(ItemStack[] outputs) {
        this.outputs = outputs;
    }

    @Override
    public String toString() {
        return "CraftingEventPacket{" +
                "containerId=" + containerId +
                ", type=" + type +
                ", recipeId=" + recipeId +
                ", inputs=" + Arrays.toString(inputs) +
                ", outputs=" + Arrays.toString(outputs) +
                '}';
    }
}
