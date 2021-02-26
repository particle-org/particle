package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class TargetNearCheck implements ICondition {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    private float checkDistance = 3 * 3;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget != null) {
            Vector3f targetPosition = this.positionService.getPosition(entityTarget);

            Vector3f position = this.positionService.getPosition(entity);

            if (targetPosition.subtract(position).lengthSquared() < checkDistance) {
                return EStatus.SUCCESS;
            } else {
                return EStatus.FAILURE;
            }
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
        if (key.equals("CheckDistance") && val.getClass() == Float.class) {
            this.checkDistance = (float) val;
            this.checkDistance = this.checkDistance * this.checkDistance;
        }
    }
}
