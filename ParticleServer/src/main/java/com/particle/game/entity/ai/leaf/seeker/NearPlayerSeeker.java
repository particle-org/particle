package com.particle.game.entity.ai.leaf.seeker;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.PlayerService;
import com.particle.model.entity.Entity;
import com.particle.model.player.Player;

import javax.inject.Inject;

public class NearPlayerSeeker implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PlayerService playerService;

    @Inject
    private PositionService positionService;

    private float checkDistance = 16;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Player closestPlayer = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Player.class);
        if (closestPlayer == null) {
            closestPlayer = this.playerService.getClosestPlayer(entity.getLevel(), this.positionService.getPosition(entity), checkDistance);
            if (closestPlayer == null) {
                return EStatus.FAILURE;
            } else {
                this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ENTITY_TARGET, closestPlayer);
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
        if (key.equals("CheckDistance") && val.getClass() == Float.class) {
            this.checkDistance = (float) val;
        }
    }

}
