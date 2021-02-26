package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.link.EntityLinkServiceProxy;
import com.particle.game.ui.TextService;
import com.particle.model.entity.Entity;
import com.particle.model.player.Player;

import javax.inject.Inject;

public class EntityLinkMountAction implements IAction {

    @Inject
    private EntityLinkServiceProxy entityLinkServiceProxy;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private TextService textService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity interactor = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_INTERACTOR, Entity.class);

        if (interactor != null) {
            this.entityLinkServiceProxy.ridingEntity(entity, interactor);
            if (interactor instanceof Player) {
                this.textService.sendTipMessage(((Player) interactor), "", 20);
            }


            return EStatus.SUCCESS;
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
