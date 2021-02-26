package com.particle.api.ui.map;

import com.particle.model.player.Player;
import com.particle.model.ui.map.MapData;
import com.particle.model.ui.map.MapDecorator;

public interface IMapDecoratorProvider {

    MapDecorator getDecorator(Player player, MapData mapData);

}
