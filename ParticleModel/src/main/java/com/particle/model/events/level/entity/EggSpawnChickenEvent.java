package com.particle.model.events.level.entity;

import com.particle.model.entity.Entity;
import com.particle.model.events.level.LevelEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;

public class EggSpawnChickenEvent extends LevelEvent {
    private Vector3f position;
    private Entity entity;

    public EggSpawnChickenEvent(Level level) {
        super(level);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
