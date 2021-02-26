package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class TargetLostCheck implements ICondition {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    private int traceDistance = 32;

    private boolean seekHeight = false;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget != null) {
            Vector3f targetPosition = this.positionService.getPosition(entityTarget);

            Vector3f position = this.positionService.getPosition(entity);

            Vector3f distanceVect = targetPosition.subtract(position);

            float distance = Math.abs(distanceVect.getX()) + Math.abs(distanceVect.getZ());
            if (seekHeight) {
                distance += Math.abs(distanceVect.getY());
            }

            if (distance > traceDistance) {
                entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.ENTITY_TARGET);
                return EStatus.SUCCESS;
            } else {
                return EStatus.FAILURE;
            }
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
        if (key.equals("TraceDistance") && val.getClass() == Integer.class) {
            this.traceDistance = (Integer) val;
        } else if (key.equals("SeekHeight") && val instanceof Boolean) {
            this.seekHeight = (boolean) val;
        }
    }
}
