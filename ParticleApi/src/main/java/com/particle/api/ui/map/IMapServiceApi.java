package com.particle.api.ui.map;

import com.particle.model.player.Player;

public interface IMapServiceApi {
    void addMapDecoratorProvider(Player player, IMapDecoratorProvider decoratorProvider, boolean isRightHand);
}
