package com.particle.game.entity.ai.leaf.seeker;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.game.world.physical.BlockColliderDetectService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class RandomMovePositionSeeker implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private LevelService levelService;

    @Inject
    private PositionService positionService;

    @Inject
    private BlockColliderDetectService blockColliderDetectService;

    private boolean seekHeight = false;

    private float range = 10;

    private BlockPrototype targetBlockType = null;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        // 搜索5次
        for (int i = 0; i < 5; i++) {
            Vector3 position = this.positionService.getFloorPosition(entity);

            position.setX(position.getX() + (int) (Math.random() * range * 2 - range));
            position.setZ(position.getZ() + (int) (Math.random() * range * 2 - range));

            if (seekHeight) {
                position.setY(position.getY() + (int) (Math.random() * range * 2 - range));
            } else {
                if (position.getY() > 255) {
                    position.setY(255);
                }

                float topCanPassHeight = this.levelService.getTopCanPassHeightBelow(entity.getLevel(), position);
                if (topCanPassHeight > 0) {
                    position.setY((int) topCanPassHeight + 1);
                } else {
                    continue;
                }
            }

            // 检查目标方块要求
            BlockPrototype blockPrototype = this.levelService.getBlockTypeAt(entity.getLevel(), position);
            if (this.targetBlockType != null && !blockPrototype.equals(this.targetBlockType)) {
                continue;
            }

            // 碰撞检测
            boolean collider = this.blockColliderDetectService.checkEntityPosition(entity, new Vector3f(position));

            if (!collider) {
                this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_TARGET, new Vector3f(position));

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
        if (key.equals("SeekHeight") && val instanceof Boolean) {
            this.seekHeight = (boolean) val;
        } else if (key.equals("Range") && val instanceof Float) {
            this.range = (float) val;
        } else if (key.equals("TargetBlockType") && val instanceof String) {
            this.targetBlockType = Block.getBlock((String) val).getType();
        }
    }
}
