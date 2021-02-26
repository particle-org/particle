package com.particle.game.entity.ai.leaf.seeker;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.link.EntityLinkServiceProxy;
import com.particle.game.entity.link.EntityMountControlService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class OwnTargetSeeker implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private EntityLinkServiceProxy entityLinkServiceProxy;

    @Inject
    private EntityMountControlService entityMountControlService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        if (entity.getLevel() == null) {
            return EStatus.FAILURE;
        }

        Entity owner = this.entityMountControlService.getOwner(entity);

        if (owner != null) {
            this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ENTITY_TARGET, owner);
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
