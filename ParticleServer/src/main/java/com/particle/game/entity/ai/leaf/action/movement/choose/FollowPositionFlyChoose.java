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

public class FollowPositionFlyChoose implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    @Inject
    private BlockColliderDetectService blockColliderDetectService;

    private float boostSpeedRate = 0;
    private float boostStartRange = 3;
    private float boostEndRange = 8;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        // 检查是否有合法目标
        Vector3f targetPosition = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.POSITION_TARGET, Vector3f.class);
        if (targetPosition != null) {

            // 获取当前位置
            Vector3f currentPosition = this.positionService.getPosition(entity);

            Vector3f moveVector = targetPosition.subtract(currentPosition);
            float moveDistance = (float) moveVector.length();

            // 近距离直接移动至目标
            float movementSpeed = this.movementServiceProxy.getMovementSpeed(entity);
            // 计算加速
            if (boostSpeedRate > 0) {
                if (moveDistance >= this.boostEndRange) {
                    movementSpeed *= 2;
                } else if (moveDistance > this.boostStartRange) {
                    movementSpeed = (moveDistance - this.boostStartRange) / (this.boostEndRange - this.boostStartRange) * (1 + this.boostSpeedRate) * movementSpeed;
                }
            }
            if (moveDistance > movementSpeed) {
                moveVector = moveVector.normalize().multiply(movementSpeed);
            }

            // 下一步移动的位置
            Vector3f destination = currentPosition.add(moveVector);

            // 检查移动位置是否合法
            if (this.blockColliderDetectService.checkEntityPosition(entity, destination)) {
                destination = currentPosition.add(0, 1, 0);
                // 检查是否可以跳跃突破障碍
                if (this.blockColliderDetectService.checkEntityPosition(entity, destination)) {
                    return EStatus.FAILURE;
                } else {
                    this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_MOVE, destination);
                    return EStatus.SUCCESS;
                }
            } else {
                this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_MOVE, destination);

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
        if (key.equalsIgnoreCase("BoostSpeedRate") && val instanceof Float) {
            this.boostSpeedRate = (Float) val;
        }
    }
}
