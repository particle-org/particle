package com.particle.api.physical;

import com.particle.model.entity.Entity;

public interface IPhysicalServiceApi {
    void initPhysicalEffects(Entity entity, boolean kinematic, boolean enableGravity);

    void initPhysicalEffects(Entity entity, boolean kinematic, boolean enableGravity, float gravity);
}
