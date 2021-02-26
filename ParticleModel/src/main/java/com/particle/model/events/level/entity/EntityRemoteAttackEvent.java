package com.particle.model.events.level.entity;

import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

public class EntityRemoteAttackEvent extends LevelEntityEvent {
    private Vector3f speed;

    public EntityRemoteAttackEvent(Entity entity, Vector3f speed) {
        super(entity);
        this.speed = speed;
    }

    public Vector3f getSpeed() {
        return speed;
    }

    public void setSpeed(Vector3f speed) {
        this.speed = speed;
    }
}
