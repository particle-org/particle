package com.particle.game.entity.ai.leaf.action.movement;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityMoveIntervalController implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    private long moveInterval = 2000L;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Long lastJumpTimestamp = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.LAST_MOVE_TIMESTAMP, Long.class);

        // 若没有超过移动CD，则跳过
        if (lastJumpTimestamp != null && System.currentTimeMillis() - lastJumpTimestamp < moveInterval) {
            return EStatus.FAILURE;
        }
        this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.LAST_MOVE_TIMESTAMP, System.currentTimeMillis());

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
        if (key.equals("MoveInterval") && val.getClass() == Long.class) {
            this.moveInterval = (long) val;
        }
    }
}
