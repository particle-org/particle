package com.particle.game.entity.ai.leaf.decision;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class PositionTargetMakerByBlindEntity implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.BLIND_DATE, Entity.class);

        if (entityTarget == null) {
            this.entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.POSITION_MOVE);
            return EStatus.FAILURE;
        } else {
            this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_TARGET, this.positionService.getPosition(entityTarget));
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
