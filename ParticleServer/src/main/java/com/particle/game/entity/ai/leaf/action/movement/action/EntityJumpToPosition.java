package com.particle.game.entity.ai.leaf.action.movement.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.animation.EntityAnimationService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.EntityEventPacket;

import javax.inject.Inject;

public class EntityJumpToPosition implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    @Inject
    private EntityAnimationService entityAnimationService;

    private float jumpForce = 5;

    private float jumpHeight = 6;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {

        // 如果在天上，则不移动
        if (!this.positionService.isOnGround(entity)) {
            return EStatus.SUCCESS;
        }

        // 检查是否有合法目标
        Vector3f targetPosition = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.POSITION_MOVE, Vector3f.class);
        if (targetPosition != null) {

            // 获取当前位置
            Vector3f currentPosition = this.positionService.getPosition(entity);
            Vector3f moveDistance = targetPosition.subtract(currentPosition);

            if (moveDistance.getY() > 0.5) {
                this.positionService.setPosition(entity, targetPosition);
                return EStatus.SUCCESS;
            }

            // 运动
            moveDistance.setY(0);
            this.movementServiceProxy.setMotion(entity, new Vector3f(moveDistance.normalize().multiply(jumpForce).add(0, jumpHeight, 0)));
            this.positionService.setDirection(entity, new Direction(moveDistance));

            // 粒子效果
            this.entityAnimationService.sendAnimation(entity, EntityEventPacket.GROUND_DUST);

            return EStatus.SUCCESS;
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
        if (key.equalsIgnoreCase("jumpForce") && val.getClass() == Float.class) {
            this.jumpForce = (float) val;
        } else if (key.equalsIgnoreCase("jumpHeight") && val.getClass() == Float.class) {
            this.jumpHeight = (float) val;
        }
    }
}
