package com.particle.game.entity.ai.leaf.seeker;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.game.world.physical.BlockColliderDetectService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import org.apache.commons.lang3.RandomUtils;

import javax.inject.Inject;

public class TransferMovePositionSeeker implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private LevelService levelService;

    @Inject
    private PositionService positionService;

    @Inject
    private BlockColliderDetectService blockColliderDetectService;

    private boolean seekHeight = false;

    private float XRange = 3;
    private float YRange = 3;
    private float ZRange = 3;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        int randomX = RandomUtils.nextInt(0, 2) != 0 ? 1 : -1;
        int randomY = RandomUtils.nextInt(0, 2) != 0 ? 1 : -1;
        int randomZ = RandomUtils.nextInt(0, 2) != 0 ? 1 : -1;

        // 搜索5次
        Vector3f position = this.positionService.getPosition(entity);

        position.setX(position.getX() + (int) (XRange * randomX));
        position.setZ(position.getZ() + (int) (ZRange * randomZ));

        if (seekHeight) {
            position.setY(position.getY() + (int) (YRange * randomY));
        } else {
            if (position.getY() > 255) {
                position.setY(255);
            }

            float topCanPassHeight = this.levelService.getTopCanPassHeightBelow(entity.getLevel(), new Vector3(position.getFloorX(), position.getFloorY(), position.getFloorZ()));
            position.setY((int) topCanPassHeight + 1);
        }

        this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_MOVE, position);

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
        if (key.equals("SeekHeight") && val instanceof Boolean) {
            this.seekHeight = (boolean) val;
        } else if (key.equals("XRange") && val instanceof Float) {
            this.XRange = (float) val;
        } else if (key.equals("YRange") && val instanceof Float) {
            this.YRange = (float) val;
        } else if (key.equals("ZRange") && val instanceof Float) {
            this.ZRange = (float) val;
        }
    }
}
