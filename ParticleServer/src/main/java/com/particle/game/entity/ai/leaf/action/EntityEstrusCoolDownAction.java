package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityEstrusCoolDownAction implements ICondition {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;


    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ESTRUS_COOL_DOWN, System.currentTimeMillis());

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