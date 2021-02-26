package com.particle.game.entity.ai.leaf.action.movement.choose;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.link.EntityMountControlService;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.physical.BlockColliderDetectService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class PlayerInputPositionChoose implements IAction {

    @Inject
    private EntityMountControlService entityMountControlService;

    @Inject
    private PositionService positionService;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    @Inject
    private BlockColliderDetectService blockColliderDetectService;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    private boolean flyMode = false;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        // 计算位移方向
        Direction movingDirection = this.entityMountControlService.getRiderAim(entity);

        // 玩家没有操作
        if (movingDirection == null) {
            return EStatus.FAILURE;
        }

        // 换算位移向量
        Vector3f movingVector = null;
        if (flyMode) {
            movingVector = movingDirection.getDirectionVector();
        } else {
            movingVector = movingDirection.getAheadDirectionVector();
        }

        // 获取生物位置
        Vector3f currentPosition = this.positionService.getPosition(entity);

        // 换算移动速度
        float movementSpeed = this.movementServiceProxy.getMovementSpeed(entity);
        if (movingVector.lengthSquared() > movementSpeed * movementSpeed) {
            // 计算速度
            movingVector = movingVector.multiply(movementSpeed);
        }

        // 检查是否可以直接移动至目标
        Vector3f destination = currentPosition.add(movingVector.getX(), movingVector.getY(), movingVector.getZ());
        destination = this.checkPosition(entity, destination, currentPosition);
        if (destination != null) {
            this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_MOVE, destination);
            return EStatus.SUCCESS;
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

            if (flyMode) {
                // 尝试绕过去
                if (!this.blockColliderDetectService.checkEntityPosition(entity, new Vector3f(destination.getX(), destination.getY(), currentPosition.getZ()))) {
                    return new Vector3f(destination.getX(), destination.getY(), currentPosition.getZ());
                } else if (!this.blockColliderDetectService.checkEntityPosition(entity, new Vector3f(destination.getX(), currentPosition.getY(), destination.getZ()))) {
                    return new Vector3f(destination.getX(), currentPosition.getY(), destination.getZ());
                } else if (!this.blockColliderDetectService.checkEntityPosition(entity, new Vector3f(currentPosition.getX(), destination.getY(), destination.getZ()))) {
                    return new Vector3f(currentPosition.getX(), destination.getY(), destination.getZ());
                }

                return null;
            } else {
                // 尝试跳过去
                if (this.blockColliderDetectService.isStandOnBlock(entity, currentPosition) && !this.blockColliderDetectService.checkEntityPosition(entity, currentPosition.add(0, 1.2f, 0))) {
                    return currentPosition.add(0, 1.2f, 0);
                } else {
                    return null;
                }
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
        if (key.equalsIgnoreCase("FlyMod") && val instanceof Boolean) {
            this.flyMode = (Boolean) val;
        }
    }
}
