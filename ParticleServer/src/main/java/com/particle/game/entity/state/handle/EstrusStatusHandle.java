package com.particle.game.entity.state.handle;

import com.particle.game.world.animation.EntityAnimationService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.type.EntityStateRecorder;
import com.particle.model.network.packets.data.EntityEventPacket;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EstrusStatusHandle implements EntityStateHandle {

    @Inject
    private EntityAnimationService entityAnimationService;


    @Override
    public String getDisplayName() {
        return "热恋";
    }

    @Override
    public void onStateEnabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {

    }

    @Override
    public void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder) {
        this.entityAnimationService.sendAnimation(entity, EntityEventPacket.LOVE_HEARTS);
    }

    @Override
    public void onStateDisabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean needDisableState) {
    }
}
