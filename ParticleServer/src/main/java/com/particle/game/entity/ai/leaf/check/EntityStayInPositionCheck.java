package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class EntityStayInPositionCheck implements ICondition {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    private boolean checkHeight = false;
    private float checkDistance = 0.1f;
    private long stayTimestamp = -1;

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
                long timestamp = System.currentTimeMillis();

                // 查询状态
                Long knowledge = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.STAY_POSITION_TIMESTAMP, Long.class);
                if (knowledge == null) {
                    knowledge = timestamp;
                    this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.STAY_POSITION_TIMESTAMP, knowledge);
                }

                // 已经停留了足够的时间
                if (timestamp - knowledge > this.stayTimestamp) {
                    this.entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.STAY_POSITION_TIMESTAMP);

                    return EStatus.SUCCESS;
                } else {
                    return EStatus.FAILURE;
                }
            } else {
                this.entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.STAY_POSITION_TIMESTAMP);

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
        } else if (key.equals("StayTimestamp") && val instanceof Long) {
            this.stayTimestamp = (long) val;
        }
    }
}
