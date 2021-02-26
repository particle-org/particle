package com.particle.network.handler.v388;

import com.particle.model.inventory.common.RecipeType;
import com.particle.model.inventory.recipe.FurnaceRecipe;
import com.particle.model.inventory.recipe.Recipe;
import com.particle.model.inventory.recipe.ShapedRecipe;
import com.particle.model.inventory.recipe.ShapelessRecipe;
import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.CraftingDataPacket;
import com.particle.network.encoder.ItemStackEncoder;
import com.particle.network.encoder.PotionMixDataEntryV388;
import com.particle.network.encoder.RecipeIngredientEncoder;
import com.particle.network.encoder.UUIDEncoder;
import com.particle.network.handler.AbstractPacketHandler;
import com.particle.network.utils.PotionMixJsonDataReader;

import java.util.List;

public class CraftingDataPacketHandler388 extends AbstractPacketHandler<CraftingDataPacket> {

    private RecipeIngredientEncoder recipeIngredientEncoder = RecipeIngredientEncoder.getInstance();

    private ItemStackEncoder itemStackEncoder = ItemStackEncoder.getInstance();

    private UUIDEncoder uuidEncoder = UUIDEncoder.getInstance();

    private PotionMixDataEntryV388 potionMixDataEntryV388 = new PotionMixDataEntryV388(PotionMixJsonDataReader.read("potions.json"));

    @Override
    protected void doDecode(CraftingDataPacket dataPacket, int version) {
    }

    @Override
    protected void doEncode(CraftingDataPacket dataPacket, int version) {
        int index = 0;

        List<Recipe> allRecipes = dataPacket.getAllRecipes();
        int size = allRecipes.size();
        dataPacket.writeUnsignedVarInt(size);
        for (Recipe recipe : allRecipes) {
            if (recipe.getType() == RecipeType.ShapelessRecipe) {
                this.encodeShapeless((ShapelessRecipe) recipe, dataPacket, version, index++);
            } else if (recipe.getType() == RecipeType.ShapedRecipe) {
                this.encodeShaped((ShapedRecipe) recipe, dataPacket, version, index++);
            } else if (recipe.getType() == RecipeType.FurnaceAuxRecipe) {
                this.encodeFurnace((FurnaceRecipe) recipe, dataPacket, version);
            }
        }

        // potion mixes
        potionMixDataEntryV388.encode(dataPacket);

        // container mixes
        dataPacket.writeUnsignedVarInt(0);

        dataPacket.writeBoolean(dataPacket.isClearRecipes());
    }

    /**
     * @param recipe
     * @param dataPacket
     */
    private void encodeShapeless(ShapelessRecipe recipe, DataPacket dataPacket, int version, int index) {
        dataPacket.writeSignedVarInt(RecipeType.ShapelessRecipe);
        dataPacket.writeString("_" + index);
        dataPacket.writeUnsignedVarInt(recipe.getInputCounts());
        List<ItemStack> inputs = recipe.getInputs();
        for (ItemStack input : inputs) {
            recipeIngredientEncoder.encode(dataPacket, input, version);
        }
        dataPacket.writeUnsignedVarInt(1);
        itemStackEncoder.encode(dataPacket, recipe.getOutput(), version);
        uuidEncoder.encode(dataPacket, recipe.getUuid(), version);
        dataPacket.writeString(recipe.getTag());
        dataPacket.writeSignedVarInt(50);
    }

    /**
     * @param recipe
     * @param dataPacket
     */
    private void encodeShaped(ShapedRecipe recipe, DataPacket dataPacket, int version, int index) {
        dataPacket.writeSignedVarInt(RecipeType.ShapedRecipe);
        dataPacket.writeString("_" + index);
        dataPacket.writeSignedVarInt(recipe.getWidth());
        dataPacket.writeSignedVarInt(recipe.getHeight());
        for (int y = 0; y < recipe.getHeight(); y++) {
            for (int x = 0; x < recipe.getWidth(); x++) {
                recipeIngredientEncoder.encode(dataPacket, recipe.getItemByPosition(x, y), version);
            }
        }
        dataPacket.writeUnsignedVarInt(1);
        itemStackEncoder.encode(dataPacket, recipe.getOutput(), version);
        uuidEncoder.encode(dataPacket, recipe.getUuid(), version);
        dataPacket.writeString(recipe.getTag());
        dataPacket.writeSignedVarInt(50);
    }

    /**
     * @param recipe
     * @param dataPacket
     */
    private void encodeFurnace(FurnaceRecipe recipe, DataPacket dataPacket, int version) {
        ItemStack input = recipe.getInput();

        dataPacket.writeSignedVarInt(recipe.getType());
        dataPacket.writeSignedVarInt(input.getItemType().getId());

        if (recipe.getType() == RecipeType.FurnaceAuxRecipe) {
            dataPacket.writeSignedVarInt(input.getMeta());
        }

        itemStackEncoder.encode(dataPacket, recipe.getOutput(), version);
        dataPacket.writeString(recipe.getTag());
    }
}
