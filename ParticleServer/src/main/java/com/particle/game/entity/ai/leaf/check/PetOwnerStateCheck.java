package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.link.EntityMountControlService;
import com.particle.game.server.Server;
import com.particle.model.entity.Entity;
import com.particle.model.player.Player;
import com.particle.model.player.PlayerState;

import javax.inject.Inject;

public class PetOwnerStateCheck implements ICondition {

    @Inject
    private Server server;

    @Inject
    private EntityMountControlService entityMountControlService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityOwn = this.entityMountControlService.getOwner(entity);

        if (entityOwn == null) {
            return EStatus.FAILURE;
        }

        if (entityOwn instanceof Player && ((Player) entityOwn).getPlayerState() == PlayerState.SPAWNED) {
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
