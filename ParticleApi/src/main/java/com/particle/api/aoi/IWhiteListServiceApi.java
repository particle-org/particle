package com.particle.api.aoi;

import com.particle.model.entity.Entity;
import com.particle.model.player.Player;

public interface IWhiteListServiceApi {
    void setWhiteListState(Entity entity, boolean state);

    void addWhiteList(Entity entity, Player player);

    void removeWhiteList(Entity entity, Player player);
}
