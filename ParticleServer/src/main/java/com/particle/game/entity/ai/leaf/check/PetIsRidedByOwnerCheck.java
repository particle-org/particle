package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.link.EntityLinkServiceProxy;
import com.particle.game.entity.link.EntityMountControlService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class PetIsRidedByOwnerCheck implements ICondition {

    @Inject
    private EntityLinkServiceProxy entityLinkServiceProxy;

    @Inject
    private EntityMountControlService entityMountControlService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityOwn = this.entityMountControlService.getOwner(entity);

        if (entityOwn == null) {
            return EStatus.FAILURE;
        }

        if (this.entityLinkServiceProxy.hasPassenger(entity, entityOwn.getRuntimeId())) {
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
