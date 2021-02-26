package com.particle.game.world.particle;

import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.ParticleType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AdvanceParticleService {

    @Inject
    private ParticleService particleService;

    public void markLine(Level level, Vector3f start, Vector3f vector, ParticleType particleType) {
        Vector3f normalize = vector.normalize().multiply(0.5f);

        float count = vector.getY() / normalize.getY();

        for (int i = 0; i < count; i++) {
            this.particleService.playParticle(level, particleType, start);
            start = start.add(normalize);
        }
    }

    public void markLines(Level level, List<Vector3f> roadPoints, ParticleType particleType) {
        Vector3f lastPoint = roadPoints.get(0);
        for (int i = 1; i < roadPoints.size(); i++) {
            this.markLine(level, lastPoint, roadPoints.get(i).subtract(lastPoint), particleType);
            lastPoint = roadPoints.get(i);
        }
    }

}
