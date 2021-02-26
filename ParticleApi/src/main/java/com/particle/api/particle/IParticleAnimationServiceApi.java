package com.particle.api.particle;

import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.CustomParticleType;

public interface IParticleAnimationServiceApi {
    void drawLine(Level level, Vector3f start, Vector3f end, CustomParticleType type);
}
