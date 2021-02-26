package com.particle.api.ui.map;

import com.particle.model.item.ItemStack;

public interface IMapGenerateServiceApi {
    ItemStack generateSingleContentMap(boolean displayPlayers, String mapName);
}
