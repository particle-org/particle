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

import javax.inject.Inject;

public class HideMovePositionSeeker implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private LevelService levelService;

    @Inject
    private PositionService positionService;

    @Inject
    private BlockColliderDetectService blockColliderDetectService;

    private boolean seekHeight = false;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityCriminal = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_CRIMINAL, Entity.class);
        if (entityCriminal == null) {
            return EStatus.FAILURE;
        }

        // 计算攻击者朝向
        Vector3f directionVector = this.positionService.getDirection(entityCriminal).getDirectionVector();

        Vector3f criminalPosition = this.positionService.getPosition(entityCriminal);

        float x = criminalPosition.getX() - directionVector.getX() * 2;
        float y = criminalPosition.getY() + directionVector.getY();
        float z = criminalPosition.getZ() - directionVector.getZ() * 2;

        Vector3f position = new Vector3f(x, y, z);

        float topCanPassHeight = this.levelService.getTopCanPassHeightBelow(entity.getLevel(), new Vector3(position.getFloorX(), position.getFloorY(), position.getFloorZ()));
        position.setY((int) topCanPassHeight + 1);

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
        }
    }
}
