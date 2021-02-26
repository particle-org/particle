package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class EntityMovePositionCheck implements ICondition {

    @Inject
    private LevelService levelService;

    @Inject
    private PositionService positionService;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    private float maxDisCanPassHeight = 2f;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        // 目标位置
        Vector3f targetPosition = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.POSITION_TARGET, Vector3f.class);
        if (targetPosition == null) {
            return EStatus.FAILURE;
        }

        // entity当前位置
        Vector3 curPosition = this.positionService.getFloorPosition(entity);

        float curTopCanPassHeight = this.levelService.getTopCanPassHeightBelow(entity.getLevel(), curPosition);
        float targetTopCanPassHeight = this.levelService.getTopCanPassHeightBelow(entity.getLevel(),
                new Vector3(targetPosition.getFloorX(), targetPosition.getFloorY(), targetPosition.getFloorZ()));

        return Math.abs(curTopCanPassHeight - targetTopCanPassHeight) <= maxDisCanPassHeight ? EStatus.SUCCESS : EStatus.FAILURE;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void config(String key, Object val) {
        if (key.equals("MaxDisCanPassHeight") && val.getClass() == Float.class) {
            this.maxDisCanPassHeight = (Float) val;
        }
    }
}
