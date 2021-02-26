package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.player.Player;

public interface VirtualEntityServiceApi {

    /**
     * 注册虚拟生物，虚拟生物不spawn进世界，每个玩家看到的都不一样
     *
     * @param entity
     */
    void registerVirtualEntity(Entity entity);

    /**
     * 去掉生物缓存，该操作会导致生物无法被交互
     *
     * @param entity
     * @return
     */
    boolean removeVirtualEntity(Entity entity);

    /**
     * 将生物刷新给玩家
     *
     * @param entity
     * @param player
     */
    void spawnToPlayer(Entity entity, Player player);

    /**
     * 将生物移除
     *
     * @param entity
     * @param player
     */
    void despawnToPlayer(Entity entity, Player player);
}
