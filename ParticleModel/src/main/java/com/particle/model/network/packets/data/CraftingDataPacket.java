package com.particle.model.network.packets.data;

import com.particle.model.inventory.recipe.Recipe;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CraftingDataPacket extends DataPacket {

    @Override
    public int pid() {
        return ProtocolInfo.CRAFTING_DATA_PACKET;
    }

    private boolean clearRecipes;

    private List<Recipe> allRecipes = new ArrayList<>();

    public boolean isClearRecipes() {
        return clearRecipes;
    }

    public void setClearRecipes(boolean clearRecipes) {
        this.clearRecipes = clearRecipes;
    }

    public List<Recipe> getAllRecipes() {
        return allRecipes;
    }

    public void addAllRecipes(Collection<Recipe> allRecipes) {
        this.allRecipes.addAll(allRecipes);
    }
}
