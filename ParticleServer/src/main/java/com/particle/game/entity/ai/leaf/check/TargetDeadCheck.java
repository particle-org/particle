package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class TargetDeadCheck implements ICondition {

    private static final int CHECK_DISTANCE = 2 * 2;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private HealthServiceProxy healthServiceProxy;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget != null) {
            if (this.healthServiceProxy.isAlive(entityTarget) && this.entitySpawnService.isSpawned(entityTarget)) {
                return EStatus.FAILURE;
            } else {
                entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.ENTITY_TARGET);
                return EStatus.SUCCESS;
            }
        }

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

    }
}
