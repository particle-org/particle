package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.api.entity.IEntityStateServiceApi;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attack.EntityAttackedHandleService;
import com.particle.game.entity.state.model.EntityStateType;
import com.particle.model.entity.Entity;
import com.particle.model.player.Player;

import javax.inject.Inject;

public class EntityBlindnessAction implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private EntityAttackedHandleService entityAttackedHandleService;

    @Inject
    private IEntityStateServiceApi entityStateServiceApi;

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget != null && entityTarget instanceof Player) {
            entityStateServiceApi.enableState(entityTarget, EntityStateType.BLINDNESS.getName(), 0, 0, 5000L);
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