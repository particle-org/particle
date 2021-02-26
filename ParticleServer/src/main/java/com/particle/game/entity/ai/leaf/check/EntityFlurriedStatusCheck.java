package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityFlurriedStatusCheck implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Boolean isFlurried = entityDecisionServiceProxy.getKnowledge(entity, Knowledge.FLURRIED_STATUS, Boolean.class);
        if (isFlurried == null || !isFlurried) {
            entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.FLURRIED_STATUS);
            return EStatus.FAILURE;
        }

        return EStatus.SUCCESS;
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
