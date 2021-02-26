package com.particle.game.entity.ai.leaf.action.movement.choose;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.physical.BlockColliderDetectService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class FollowPositionFlutterChoose implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

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

            Vector3f destination = currentPosition.add(new Vector3f(0, 0.2f, 0));
            if (!this.blockColliderDetectService.checkEntityPosition(entity, destination)) {
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

    }
}
