package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.entity.type.EntityStateRecorder;

public interface IEntityStateServiceApi {
    void enableState(Entity entity, String name, int level, long updateInterval, long ttl);

    void enableState(Entity source, Entity entity, String name, int level, long updateInterval, long ttl);

    void disableState(Entity entity, String name);

    boolean hasState(Entity entity, String name);

    EntityStateRecorder getState(Entity entity, String name);
}
