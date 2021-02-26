package com.particle.game.entity.ai.leaf.seeker;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Direction;

import javax.inject.Inject;

public class RandomPositionSeeker implements IAction {

    @Inject
    private PositionService positionService;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    private boolean seekHeight = false;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Direction direction = this.positionService.getDirection(entity);
        direction.setYaw((float) (Math.random() * 30 - 15) + direction.getYaw());
        direction.setYawHead(direction.getYaw());
        if (seekHeight) {
            direction.setPitch((float) (Math.random() * 30 - 15) + direction.getPitch());
        }

        this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.POSITION_TARGET, this.positionService.getPosition(entity).add(direction.getDirectionVector()));

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
