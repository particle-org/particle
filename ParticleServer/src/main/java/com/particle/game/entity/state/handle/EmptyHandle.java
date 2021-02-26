package com.particle.game.entity.state.handle;

import com.particle.model.entity.Entity;
import com.particle.model.entity.type.EntityStateRecorder;

import javax.inject.Singleton;

@Singleton
public class EmptyHandle implements EntityStateHandle {

    @Override
    public String getDisplayName() {
        return "";
    }

    @Override
    public void onStateEnabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {

    }

    @Override
    public void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder) {

    }

    @Override
    public void onStateDisabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean needDisableState) {

    }
}
