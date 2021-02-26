package com.particle.game.entity.ai.leaf.seeker;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class NearBlockSeeker implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    @Inject
    private LevelService levelService;

    private BlockPrototype targetBlock;
    private int searchRadius = 4;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        if (this.targetBlock == null) {
            return EStatus.FAILURE;
        }

        Vector3f entityPosition = this.positionService.getPosition(entity);

        int distance = Integer.MAX_VALUE;
        Vector3 targetPosition = null;

        for (int i = -searchRadius; i <= searchRadius; i++) {
            for (int j = -searchRadius; j <= searchRadius; j++) {
                BlockPrototype blockAt = this.levelService.getBlockTypeAt(entity.getLevel(), entityPosition.getFloorX() + i, entityPosition.getFloorY(), entityPosition.getFloorZ() + j);

                if (blockAt.equals(targetBlock)) {
                    if (distance > i + j) {
                        distance = i + j;
                        targetPosition = new Vector3(entityPosition.getFloorX() + i, entityPosition.getFloorY(), entityPosition.getFloorZ() + j);
                    }
                }
            }
        }

        if (targetPosition != null) {
            this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_TARGET, new Vector3f(targetPosition).add(0.5f, 0, 0.5f));
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
        if (key.equals("TargetBlock") && val.getClass() == String.class) {
            Block block = Block.getBlock((String) val);
            if (block != null) {
                this.targetBlock = block.getType();
            }
        } else if (key.equals("SearchDistance") && val.getClass() == Integer.class) {
            this.searchRadius = (int) val;
        }
    }

}
