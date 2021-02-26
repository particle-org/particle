package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityCriminalCheck implements ICondition {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);
        Entity entityCriminal = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_CRIMINAL, Entity.class);
        if (entityTarget.getRuntimeId() == entityCriminal.getRuntimeId()) {
            return EStatus.SUCCESS;
        } else {
            return EStatus.FAILURE;
        }
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
