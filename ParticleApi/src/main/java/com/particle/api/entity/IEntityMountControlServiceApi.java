package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.player.Player;

public interface IEntityMountControlServiceApi {
    void boundMountEntity(Player player, Entity entity);
}
