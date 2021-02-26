package com.particle.network.handler.v410;

import com.particle.model.inventory.common.RecipeType;
import com.particle.model.inventory.recipe.FurnaceRecipe;
import com.particle.model.inventory.recipe.Recipe;
import com.particle.model.inventory.recipe.ShapedRecipe;
import com.particle.model.inventory.recipe.ShapelessRecipe;
import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.CraftingDataPacket;
import com.particle.network.encoder.ItemStackEncoder410;
import com.particle.network.encoder.PotionMixDataEntryV410;
import com.particle.network.encoder.RecipeIngredientEncoder;
import com.particle.network.encoder.UUIDEncoder;
import com.particle.network.handler.AbstractPacketHandler;
import com.particle.network.utils.PotionMixJsonDataReader;

import java.util.List;

public class CraftingDataPacketHandler410 extends AbstractPacketHandler<CraftingDataPacket> {

    private RecipeIngredientEncoder recipeIngredientEncoder = RecipeIngredientEncoder.getInstance();

    private ItemStackEncoder410 itemStackEncoder410 = ItemStackEncoder410.getInstance();

    private UUIDEncoder uuidEncoder = UUIDEncoder.getInstance();

    private PotionMixDataEntryV410 potionMixDataEntryV410 = new PotionMixDataEntryV410(PotionMixJsonDataReader.read("potionsV410.json"));

    @Override
    protected void doDecode(CraftingDataPacket dataPacket, int version) {
    }

    @Override
    protected void doEncode(CraftingDataPacket dataPacket, int version) {
        int index = 0;

//        dataPacket.writeUnsignedVarInt(0);

        List<Recipe> allRecipes = dataPacket.getAllRecipes();
        int size = allRecipes.size();
        int netId = 1;
        dataPacket.writeUnsignedVarInt(size);
        for (Recipe recipe : allRecipes) {
            if (recipe.getType() == RecipeType.ShapelessRecipe) {
                this.encodeShapeless((ShapelessRecipe) recipe, dataPacket, version, index++);
                dataPacket.writeUnsignedVarInt(netId++);
            } else if (recipe.getType() == RecipeType.ShapedRecipe) {
                this.encodeShaped((ShapedRecipe) recipe, dataPacket, version, index++);
                dataPacket.writeUnsignedVarInt(netId++);
            } else if (recipe.getType() == RecipeType.FurnaceAuxRecipe) {
                this.encodeFurnace((FurnaceRecipe) recipe, dataPacket, version);
            }
        }

        // potion mixes
        potionMixDataEntryV410.encode(dataPacket);

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
        itemStackEncoder410.encode(dataPacket, recipe.getOutput(), version);
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
        itemStackEncoder410.encode(dataPacket, recipe.getOutput(), version);
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

        itemStackEncoder410.encode(dataPacket, recipe.getOutput(), version);
        dataPacket.writeString(recipe.getFurnaceTag());
    }
}
