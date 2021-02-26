package com.particle.game.entity.ai.leaf.action.attack;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.attack.ExplosionService;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityExplosion implements IAction {

    @Inject
    private ExplosionService explosionService;

    @Inject
    private PositionService positionService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        this.explosionService.explosionAt(entity.getLevel(), entity, this.positionService.getPosition(entity), 5);

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
