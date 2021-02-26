package com.particle.game.entity.ai.leaf.decision;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class SetCriminalAsEntityTarget implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_CRIMINAL, Entity.class);

        if (entityTarget != null) {
            this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ENTITY_TARGET, entityTarget);
            this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.REMOTE_ATTACK_POWER, 0f);

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
