package com.particle.game.entity.ai.leaf.decision;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityDecisionMaker implements IAction {

    private String decisionName = null;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        if (this.decisionName == null) {
            return EStatus.FAILURE;
        }

        this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.STATE_TIMESTAMP, System.currentTimeMillis());

        this.entityDecisionServiceProxy.updateActionDecision(entity, decisionName);

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
        if (key.equals("Decision") && val instanceof String) {
            this.decisionName = (String) val;
        }
    }
}
