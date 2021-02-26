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

public class EscapeEntityPositionChoose implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    @Inject
    private BlockColliderDetectService blockColliderDetectService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget == null) {
            this.entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.POSITION_MOVE);
        } else {
            Vector3f targetPosition = this.positionService.getPosition(entityTarget);
            Vector3f currentPosition = this.positionService.getPosition(entity);

            Vector3f moveDirectionVector = currentPosition.subtract(targetPosition);
            moveDirectionVector.setY(0);
            // 换算移动速度
            float movementSpeed = this.movementServiceProxy.getMovementSpeed(entity) / 2;
            moveDirectionVector = moveDirectionVector.normalize().multiply(movementSpeed);

            // 检查是否可以直接移动至目标
            Vector3f destination = currentPosition.add(moveDirectionVector);
            destination = this.checkPosition(entity, destination);
            if (destination != null) {
                this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_MOVE, destination);
                return EStatus.SUCCESS;
            }
        }

        return EStatus.FAILURE;
    }

    private Vector3f checkPosition(Entity entity, Vector3f destination) {
        // 检查移动位置是否合法
        if (this.blockColliderDetectService.checkEntityPosition(entity, destination)) {
            destination = destination.add(0, 1, 0);
            // 检查是否可以跳跃突破障碍
            if (!this.blockColliderDetectService.checkEntityPosition(entity, destination)) {
                return destination;
            } else {
                return null;
            }
        } else {
            return destination;
        }
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
