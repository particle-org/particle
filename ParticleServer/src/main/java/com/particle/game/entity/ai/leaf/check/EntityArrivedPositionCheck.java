package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class EntityArrivedPositionCheck implements ICondition {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    private boolean checkHeight = false;
    private float checkDistance = 0.1f;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Vector3f targetPosition = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.POSITION_TARGET, Vector3f.class);

        if (targetPosition != null) {
            Vector3f distance = targetPosition.subtract(this.positionService.getPosition(entity));
            if (!this.checkHeight) {
                distance.setY(0);
            }

            if (distance.lengthSquared() < checkDistance) {
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
        if (key.equals("CheckHeight") && val instanceof Boolean) {
            this.checkHeight = (boolean) val;
        } else if (key.equals("CheckDistance") && val instanceof Float) {
            this.checkDistance = (float) val;
        }
    }
}
