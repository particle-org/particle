package com.particle.game.entity.ai.leaf.action.movement.choose;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.physical.BlockColliderDetectService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class FollowPositionMonsterChoose implements IAction {
    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    @Inject
    private BlockColliderDetectService blockColliderDetectService;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Vector3f targetPosition = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.POSITION_TARGET, Vector3f.class);

        if (targetPosition == null) {
            this.entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.POSITION_TARGET);
        } else {
            Vector3f currentPosition = this.positionService.getPosition(entity);
            //下一tick位置
            float movementSpeed = this.movementServiceProxy.getMovementSpeed(entity);
            Vector3f moveDirectionVector = currentPosition.subtract(targetPosition);
            moveDirectionVector = moveDirectionVector.multiply(movementSpeed);
            Vector3f destination = currentPosition.subtract(moveDirectionVector);
            this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_MOVE, destination);
        }
        return null;
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
