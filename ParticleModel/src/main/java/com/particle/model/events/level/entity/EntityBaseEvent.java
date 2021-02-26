package com.particle.model.events.level.entity;

import com.particle.model.entity.Entity;
import com.particle.model.events.level.player.LevelPlayerEvent;
import com.particle.model.player.Player;

public abstract class EntityBaseEvent extends LevelPlayerEvent {
    private Entity entityId;

    public EntityBaseEvent(Player player, Entity entityId) {
        super(player);
        this.entityId = entityId;
    }

    public Entity getEntity() {
        return entityId;
    }
}
