package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.mob.MobEntity;

import javax.inject.Inject;

public class EntityBlindDateCheck implements ICondition {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        MobEntity target = entityDecisionServiceProxy.getKnowledge(entity, Knowledge.BLIND_DATE, MobEntity.class);

        if (target != null && (entity instanceof MobEntity && target.getNetworkId() == ((MobEntity) entity).getNetworkId())) {
            movementServiceProxy.setRunning(entity, true);
            return EStatus.SUCCESS;
        } else {
            movementServiceProxy.setRunning(entity, false);
            return EStatus.FAILURE;
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
