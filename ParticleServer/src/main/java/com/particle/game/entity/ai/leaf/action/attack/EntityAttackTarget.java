package com.particle.game.entity.ai.leaf.action.attack;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attack.EntityAttackedHandleService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityAttackTarget implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private EntityAttackedHandleService entityAttackedHandleService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget != null) {
            this.entityAttackedHandleService.entityAttackedByEntity(entity, entityTarget);
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
