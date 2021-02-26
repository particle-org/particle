package com.particle.game.entity.movement;

import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.model.entity.Entity;

public class MovementService {
    /**
     * 获取生物移动速度
     *
     * @param entity
     * @return
     */
    public static float getMovementSpeed(Entity entity, EntityMovementModule entityMovementModule) {
        if (entityMovementModule.isRunning()) {
            return entityMovementModule.getMaxSpeed();
        } else {
            return entityMovementModule.getSpeed();
        }
    }
}
