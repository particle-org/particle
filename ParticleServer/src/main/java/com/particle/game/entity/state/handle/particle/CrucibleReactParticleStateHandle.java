package com.particle.game.entity.state.handle.particle;

import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.state.handle.EntityStateHandle;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.type.EntityStateRecorder;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.CustomParticleType;
import com.particle.model.particle.ParticleType;

import javax.inject.Inject;

public class CrucibleReactParticleStateHandle implements EntityStateHandle {

    @Inject
    private ParticleService particleService;

    @Inject
    private PositionService positionService;

    @Override
    public String getDisplayName() {
        return "CrucibleReact";
    }

    @Override
    public void onStateEnabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean hasEnabled) {
        Vector3f position = this.positionService.getPosition(entity);

        this.particleService.playParticle(entity.getLevel(), CustomParticleType.CAULDRON_EXPLOSION_EMITTER, position.add(0.5f, 1, 0.5f));
    }

    @Override
    public void onStateUpdated(Entity entity, EntityStateRecorder entityStateRecorder) {
        Vector3f position = this.positionService.getPosition(entity).add(0f, 1f, 0f);

        this.particleService.playParticle(entity.getLevel(), ParticleType.TYPE_EXPLODE, position.add((float) Math.random(), 0, (float) Math.random()));
        this.particleService.playParticle(entity.getLevel(), ParticleType.TYPE_EXPLODE, position.add((float) Math.random(), 0, (float) Math.random()));
        this.particleService.playParticle(entity.getLevel(), ParticleType.TYPE_EXPLODE, position.add((float) Math.random(), 0, (float) Math.random()));
    }

    @Override
    public void onStateDisabled(Entity entity, EntityStateRecorder entityStateRecorder, boolean needDisableState) {

    }
}
