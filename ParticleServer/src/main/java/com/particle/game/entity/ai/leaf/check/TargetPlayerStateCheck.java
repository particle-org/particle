package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.player.PlayerService;
import com.particle.model.entity.Entity;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;
import com.particle.model.player.PlayerState;

import javax.inject.Inject;

public class TargetPlayerStateCheck implements ICondition {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PlayerService playerService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget instanceof Player) {
            Player player = (Player) entityTarget;

            if (player.getPlayerState() == PlayerState.SPAWNED
                    // TODO: 看一下这里能否去掉GameMode检查
                    && playerService.getGameMode(player) == GameMode.SURVIVE) {
                return EStatus.SUCCESS;
            }

            return EStatus.FAILURE;
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
