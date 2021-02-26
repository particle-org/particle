package com.particle.game.entity.ai.leaf.decision;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class SetEntityInvincibleTime implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    // 无敌时间，时间单位ms
    private long invincibleTime = 5000L;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        long curTime = System.currentTimeMillis();
        entityDecisionServiceProxy.addKnowledge(entity, Knowledge.INVINCIBLE_TIME, curTime + invincibleTime);

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
        if (key.equalsIgnoreCase("InvincibleTime") && val instanceof Long) {
            this.invincibleTime = (Long) val;
        }
    }
}
