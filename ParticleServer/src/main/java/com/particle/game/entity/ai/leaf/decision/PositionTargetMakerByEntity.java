package com.particle.game.entity.ai.leaf.decision;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class PositionTargetMakerByEntity implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    private float offsetY = 0;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget == null) {
            this.entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.POSITION_MOVE);
            return EStatus.FAILURE;
        } else {
            this.entityDecisionServiceProxy.addKnowledge(
                    entity,
                    Knowledge.POSITION_TARGET,
                    this.positionService.getPosition(entityTarget).add(0, offsetY, 0));
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
        if (key.equalsIgnoreCase("OffsetY") && val instanceof Float) {
            this.offsetY = (Float) val;
        }
    }
}
