package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.link.EntityLinkServiceProxy;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class PetHasRiderCheck implements ICondition {

    @Inject
    private EntityLinkServiceProxy entityLinkServiceProxy;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        if (entityLinkServiceProxy.hasPassenger(entity)) {
            return EStatus.SUCCESS;
        }

        return EStatus.FAILURE;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {
    }
}
