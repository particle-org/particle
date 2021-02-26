package com.particle.game.world.animation;

import com.particle.api.particle.IParticleAnimationServiceApi;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.CustomParticleType;

import javax.inject.Singleton;

@Singleton
public class ParticleAnimationServiceProxy implements IParticleAnimationServiceApi {

    @Override
    public void drawLine(Level level, Vector3f start, Vector3f end, CustomParticleType type) {
        ParticleAnimationService.drawLineAnimate(level, start, end, type, 0.3f);
    }

}
