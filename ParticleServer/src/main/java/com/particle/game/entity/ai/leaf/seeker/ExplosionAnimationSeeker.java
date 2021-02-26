package com.particle.game.entity.ai.leaf.seeker;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.world.animation.EntityAnimationService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class ExplosionAnimationSeeker implements IAction {

    @Inject
    private EntityAnimationService entityAnimationService;

    private boolean isExploding;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        entityAnimationService.exploding(entity, isExploding);

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
        if (key.equalsIgnoreCase("isExploding") && val instanceof Boolean) {
            isExploding = (Boolean) val;
        }
    }

}
