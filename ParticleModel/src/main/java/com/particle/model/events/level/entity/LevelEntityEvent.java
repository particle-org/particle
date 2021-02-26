package com.particle.model.events.level.entity;

import com.particle.model.entity.Entity;
import com.particle.model.events.level.LevelEvent;

public abstract class LevelEntityEvent extends LevelEvent {
    private Entity entity;

    public LevelEntityEvent(Entity entity) {
        super(entity.getLevel());

        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
