package com.particle.game.world.map.providers;

import com.particle.model.player.Player;
import com.particle.model.ui.map.MapData;

public interface IMapProvider {

    MapData getMapData(Player player, long mapId);

}
