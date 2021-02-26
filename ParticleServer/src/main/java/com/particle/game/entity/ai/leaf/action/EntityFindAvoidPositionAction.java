package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class EntityFindAvoidPositionAction implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    private float checkDistance = 16;
    private float distanceSquared = 16;

    @Override
    public void onInitialize() {
        distanceSquared = checkDistance * checkDistance;
    }

    @Override
    public EStatus tick(Entity entity) {
        Entity closestEntity = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);
        if (closestEntity != null) {
            double distance = positionService.getPosition(entity).subtract(positionService.getPosition(closestEntity)).lengthSquared();
            if (distance <= distanceSquared) {
                Vector3f unitVector = positionService.getPosition(entity).subtract(positionService.getPosition(closestEntity)).normalize();
                Vector3f avoidPosition = positionService.getPosition(entity).add(unitVector.getX() * (checkDistance), 0, unitVector.getZ() * (checkDistance));
                entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_TARGET, avoidPosition);

                return EStatus.SUCCESS;
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
        }
    }
}