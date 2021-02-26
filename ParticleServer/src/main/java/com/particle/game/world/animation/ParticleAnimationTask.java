package com.particle.game.world.animation;

import com.particle.api.inject.RequestStaticInject;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.CustomParticleType;

import javax.inject.Inject;

@RequestStaticInject
public class ParticleAnimationTask {

    @Inject
    private static ParticleService particleService;

    private Level level;
    private CustomParticleType type;
    private Vector3f current;
    private double length;
    private Vector3f step;
    private double stepLength;

    public ParticleAnimationTask(Level level, CustomParticleType type, Vector3f start, Vector3f end, float stepLength) {
        this.level = level;
        this.type = type;
        this.current = start;

        this.step = end.subtract(start).normalize().multiply(stepLength);
        this.length = end.subtract(start).length();
        this.stepLength = stepLength;
    }

    public boolean tick() {
        particleService.playParticle(level, type, current);

        this.length -= this.stepLength;
        if (this.length < 0) {
            return false;
        }

        this.current = this.current.add(step);
        return true;
    }

}
