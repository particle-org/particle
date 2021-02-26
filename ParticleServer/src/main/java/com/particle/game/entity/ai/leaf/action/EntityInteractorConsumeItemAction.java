package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.item.DurabilityService;
import com.particle.model.entity.Entity;
import com.particle.model.player.Player;

import javax.inject.Inject;

public class EntityInteractorConsumeItemAction implements IAction {

    @Inject
    private DurabilityService durabilityService;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity interactor = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_INTERACTOR, Entity.class);

        if (interactor instanceof Player) {
            this.durabilityService.consumptionItem(interactor);
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
