package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityEstrusAction implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Boolean entityEstrusStatus = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ESTRUS_STATUS, Boolean.class);
        if (entityEstrusStatus != null && entityEstrusStatus) {
            return EStatus.FAILURE;
        }

        this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ESTRUS_STATUS, true);

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