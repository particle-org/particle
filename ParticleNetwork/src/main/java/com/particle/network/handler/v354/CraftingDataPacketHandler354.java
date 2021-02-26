package com.particle.network.handler.v354;

import com.particle.model.inventory.common.RecipeType;
import com.particle.model.inventory.recipe.FurnaceRecipe;
import com.particle.model.inventory.recipe.Recipe;
import com.particle.model.inventory.recipe.ShapedRecipe;
import com.particle.model.inventory.recipe.ShapelessRecipe;
import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.CraftingDataPacket;
import com.particle.network.encoder.ItemStackEncoder;
import com.particle.network.encoder.UUIDEncoder;
import com.particle.network.handler.AbstractPacketHandler;

import java.util.List;

public class CraftingDataPacketHandler354 extends AbstractPacketHandler<CraftingDataPacket> {

    private ItemStackEncoder itemStackEncoder = ItemStackEncoder.getInstance();

    private UUIDEncoder uuidEncoder = UUIDEncoder.getInstance();

    @Override
    protected void doDecode(CraftingDataPacket dataPacket, int version) {
    }

    @Override
    protected void doEncode(CraftingDataPacket dataPacket, int version) {
        List<Recipe> allRecipes = dataPacket.getAllRecipes();
        int size = allRecipes.size();
        dataPacket.writeUnsignedVarInt(size);
        for (Recipe recipe : allRecipes) {
            if (recipe.getType() == RecipeType.ShapelessRecipe) {
                this.encodeShapeless((ShapelessRecipe) recipe, dataPacket, version);
            } else if (recipe.getType() == RecipeType.ShapedRecipe) {
                this.encodeShaped((ShapedRecipe) recipe, dataPacket, version);
            } else if (recipe.getType() == RecipeType.FurnaceAuxRecipe) {
                this.encodeFurnace((FurnaceRecipe) recipe, dataPacket, version);
            } else if (recipe.getType() == RecipeType.MultiRecipe) {
                // TODO
            } else if (recipe.getType() == RecipeType.ShulkerBoxRecipe) {
                // TODO 潜影盒
            }
        }
        dataPacket.writeBoolean(dataPacket.isClearRecipes());
    }

    /**
     * @param recipe
     * @param dataPacket
     */
    private void encodeShapeless(ShapelessRecipe recipe, DataPacket dataPacket, int version) {

        dataPacket.writeSignedVarInt(RecipeType.ShapelessRecipe);
        dataPacket.writeUnsignedVarInt(recipe.getInputCounts());
        List<ItemStack> inputs = recipe.getInputs();
        for (ItemStack input : inputs) {
            itemStackEncoder.encode(dataPacket, input, version);
        }
        dataPacket.writeUnsignedVarInt(1);
        itemStackEncoder.encode(dataPacket, recipe.getOutput(), version);
        uuidEncoder.encode(dataPacket, recipe.getUuid(), version);
        dataPacket.writeString(recipe.getTag());
    }

    /**
     * @param recipe
     * @param dataPacket
     */
    private void encodeShaped(ShapedRecipe recipe, DataPacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(RecipeType.ShapedRecipe);
        dataPacket.writeSignedVarInt(recipe.getWidth());
        dataPacket.writeSignedVarInt(recipe.getHeight());
        for (int y = 0; y < recipe.getHeight(); y++) {
            for (int x = 0; x < recipe.getWidth(); x++) {
                itemStackEncoder.encode(dataPacket, recipe.getItemByPosition(x, y), version);
            }
        }
        dataPacket.writeUnsignedVarInt(1);
        itemStackEncoder.encode(dataPacket, recipe.getOutput(), version);
        uuidEncoder.encode(dataPacket, recipe.getUuid(), version);
        dataPacket.writeString(recipe.getTag());
    }

    /**
     * @param recipe
     * @param dataPacket
     */
    private void encodeFurnace(FurnaceRecipe recipe, DataPacket dataPacket, int version) {
        ItemStack input = recipe.getInput();
        if (input.hasMeta()) {
            dataPacket.writeUnsignedVarInt(RecipeType.FurnaceAuxRecipe);
            dataPacket.writeSignedVarInt(input.getItemType().getId());
            dataPacket.writeSignedVarInt(input.getMeta());
            itemStackEncoder.encode(dataPacket, recipe.getOutput(), version);
        } else {
            dataPacket.writeUnsignedVarInt(RecipeType.FurnaceRecipe);
            dataPacket.writeSignedVarInt(input.getItemType().getId());
            itemStackEncoder.encode(dataPacket, recipe.getOutput(), version);
        }
        dataPacket.writeString(recipe.getTag());
    }
}
