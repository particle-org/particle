package com.particle.game.entity.ai.leaf.decision;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class SetEntityFlurriedStatus implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    private boolean isFlurried = false;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        movementServiceProxy.setRunning(entity, isFlurried);
        entityDecisionServiceProxy.addKnowledge(entity, Knowledge.FLURRIED_STATUS, isFlurried);
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
        if (key.equalsIgnoreCase("isFlurried") && val instanceof Boolean) {
            this.isFlurried = (Boolean) val;
        }
    }
}
