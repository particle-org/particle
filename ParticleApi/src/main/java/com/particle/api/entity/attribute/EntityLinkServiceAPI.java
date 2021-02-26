package com.particle.api.entity.attribute;

import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import java.util.List;

public interface EntityLinkServiceAPI {

    /**
     * 设置骑乘
     *
     * @param rided  被骑乘
     * @param riding 骑乘者
     */
    void ridingEntity(Entity rided, Entity riding);

    /**
     * 设置骑乘
     *
     * @param vehicle
     * @param passenger
     * @param sitOffset
     */
    void ridingEntity(Entity vehicle, Entity passenger, Vector3f sitOffset);

    /**
     * 取消骑乘
     *
     * @param rided 被骑乘
     */
    void unMountEntity(Entity rided);

    /**
     * 取消骑乘
     *
     * @param riding 骑乘者
     */
    void unRideEntity(Entity riding);

    /**
     * 查询骑乘的生物
     *
     * @param player 玩家
     */
    @Deprecated
    long getPlayerRidingEntityId(Player player);

    /**
     * 查询骑乘的生物
     *
     * @param entity
     */
    Entity getVehicle(Entity entity);

    /**
     * 查询骑乘者
     *
     * @param entity
     * @return
     */
    Entity getRiderEntity(Entity entity);

    /**
     * 查询骑乘者
     *
     * @param entity
     * @return
     */
    List<Entity> getRiderEntities(Entity entity);
}
