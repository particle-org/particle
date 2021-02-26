package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.player.Player;

public interface IDynamicDisplayNameRefreshServiceApi {
    void bindModule(Entity entity, String template, long refreshInterval);

    void requestRefresh(Entity entity);

    void refreshToPlayer(Entity entity, Player player);
}
