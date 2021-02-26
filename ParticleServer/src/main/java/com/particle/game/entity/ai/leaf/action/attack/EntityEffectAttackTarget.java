package com.particle.game.entity.ai.leaf.action.attack;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.state.EntityStateService;
import com.particle.model.effect.EffectBaseType;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityEffectAttackTarget implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private EntityStateService entityStateService;

    private EffectBaseType effect;
    private long duration = 3;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        if (effect == null) {
            return EStatus.FAILURE;
        }

        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget != null) {
            this.entityStateService.enableState(entityTarget, effect.getName(), 1, -1, duration * 1000);
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
        if (key.equals("EffectType") && val instanceof String) {
            this.effect = EffectBaseType.valueOf((String) val);
        } else if (key.equals("Duration") && val instanceof Long) {
            this.duration = (Long) val;
        }
    }
}
