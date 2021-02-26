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

public class FollowPositionByPassChoose implements IAction {

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
        Vector3f targetPosition = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.POSITION_TARGET, Vector3f.class);

        if (targetPosition == null) {
            this.entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.POSITION_TARGET);
        } else {
            Vector3f currentPosition = this.positionService.getPosition(entity);

            Vector3f moveDistance = targetPosition.subtract(currentPosition);
            moveDistance.setY(0);
            // 换算移动速度
            float movementSpeed = this.movementServiceProxy.getMovementSpeed(entity);
            if (moveDistance.lengthSquared() > movementSpeed * movementSpeed) {
                // 计算速度
                moveDistance = moveDistance.normalize().multiply(movementSpeed);
            }

            // 检查是否可以直接移动至目标
            Vector3f destination = currentPosition.add(moveDistance);
            destination = this.checkPosition(entity, destination, currentPosition);
            if (destination != null) {
                this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_MOVE, destination);
                return EStatus.SUCCESS;
            }

            // 计算绕路的方案
            Boolean rightOnStock = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.RIGHT_STOCKED, Boolean.class);
            if (rightOnStock != null && rightOnStock) {
                // 顺时针转90
                destination = currentPosition.add(new Vector3f(moveDistance.getZ(), moveDistance.getY(), -moveDistance.getX()));
                destination = checkPosition(entity, destination, currentPosition);

                if (destination == null) {
                    this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.RIGHT_STOCKED, false);
                    this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.STOCKED, true);
                    return EStatus.FAILURE;
                }

                this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_MOVE, destination);
                return EStatus.SUCCESS;
            } else {
                // 逆时针转90
                destination = currentPosition.add(new Vector3f(-moveDistance.getZ(), moveDistance.getY(), moveDistance.getX()));
                destination = checkPosition(entity, destination, currentPosition);

                if (destination == null) {
                    this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.RIGHT_STOCKED, true);
                    this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.STOCKED, true);
                    return EStatus.FAILURE;
                }

                this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_MOVE, destination);
                return EStatus.SUCCESS;
            }
        }

        return EStatus.FAILURE;
    }

    private Vector3f checkPosition(Entity entity, Vector3f destination, Vector3f currentPosition) {
        // 检查移动位置是否合法
        if (this.blockColliderDetectService.checkEntityPosition(entity, destination)) {
            // 发生碰撞
            // 看看当前是不是卡在方块里面了，是的话就允许移动
            if (this.blockColliderDetectService.checkEntityPosition(entity, currentPosition)) {
                return destination;
            }

            // 如果没有卡主，则尝试跳跃过去
            if (this.blockColliderDetectService.isStandOnBlock(entity, currentPosition) && !this.blockColliderDetectService.checkEntityPosition(entity, currentPosition.add(0, 1.2f, 0))) {
                return currentPosition.add(0, 1.2f, 0);
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
