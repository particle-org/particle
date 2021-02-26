package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.level.Level;

public interface IEntityRuntimeSpawnServiceApi {
    void spawn(Level level, Entity entity);

    void despawn(Entity entity);
}
