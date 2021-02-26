package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.player.Player;

public interface EntityAttackedHandleServiceApi {
    /**
     * 查询生物的攻击者
     *
     * @param entity
     * @return
     */
    public Player getEntityAttacker(Entity entity);
}
