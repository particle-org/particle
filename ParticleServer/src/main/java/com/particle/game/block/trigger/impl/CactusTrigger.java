package com.particle.game.block.trigger.impl;

import com.particle.game.block.trigger.components.IEntityEnterBlockTrigger;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.model.entity.Entity;
import com.particle.model.events.level.entity.EntityDamageType;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CactusTrigger implements IEntityEnterBlockTrigger {

    @Inject
    private HealthServiceProxy healthServiceProxy;

    @Override
    public void trigger(Entity entity) {
        this.healthServiceProxy.damageEntity(entity, 0.5f, EntityDamageType.Cactus, null);
    }
}
