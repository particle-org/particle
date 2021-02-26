package com.particle.api.entity.attribute;

import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

public interface MovementServiceAPI {

    /**
     * 增加玩家的速度
     *
     * @param entity
     * @param position
     */
    void setMotion(Entity entity, Vector3f position);

    /**
     * 增加玩家的速度
     *
     * @param entity
     * @param motion
     */
    void addMotion(Entity entity, Vector3f motion);

    /**
     * 获取motion
     *
     * @param entity
     * @return
     */
    Vector3f getMotion(Entity entity);

    /**
     * 更新motion
     *
     * @param entity
     * @param speed
     * @param maxSpeed
     */
    void updateMovement(Entity entity, float speed, float maxSpeed);

    /**
     * 获取entity的移动速度
     *
     * @param entity
     */
    float getMovementSpeed(Entity entity);
}
