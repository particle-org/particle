package com.particle.game.entity.ai.leaf.seeker;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.PlayerService;
import com.particle.model.entity.Entity;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;

import javax.inject.Inject;

public class NearGmTargetSeeker implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PlayerService playerService;

    @Inject
    private PositionService positionService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Player closestPlayer = this.playerService.getClosestPlayer(entity.getLevel(), GameMode.CREATIVE, this.positionService.getPosition(entity), 16);

        if (closestPlayer != null) {
            this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ENTITY_TARGET, closestPlayer);
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
