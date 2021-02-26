package com.particle.game.world.animation;

import com.particle.api.inject.RequestStaticInject;
import com.particle.game.world.level.WorldService;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.CustomParticleType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Singleton
@RequestStaticInject
public class ParticleAnimationService {

    @Inject
    private static WorldService worldService;

    @Inject
    private static ParticleService particleService;

    private static List<ParticleAnimationTask> particleAnimationTasks;

    /**
     * 画一条线
     *
     * @param level
     * @param start
     * @param end
     * @param type
     */
    public static void drawLine(Level level, Vector3f start, Vector3f end, CustomParticleType type) {
        Vector3f ray = end.subtract(start);
        Vector3f step = ray.normalize();
        double length = ray.length();

        for (int i = 0; i < length; i++) {
            start = start.add(step);

            particleService.playParticle(level, type, start);
        }
    }

    public static void drawLineAnimate(Level level, Vector3f start, Vector3f end, CustomParticleType type, float step) {
        checkQueue();

        particleAnimationTasks.add(new ParticleAnimationTask(level, type, start, end, step));
    }

    private static void checkQueue() {
        if (particleAnimationTasks == null) {
            particleAnimationTasks = Collections.synchronizedList(new LinkedList<>());
            worldService.getDefaultLevel().getLevelSchedule().scheduleRepeatingTask("PlayParticle", () -> {
                int tickAmount = particleAnimationTasks.size();
                for (int i = 0; i < tickAmount; i++) {
                    ParticleAnimationTask task = particleAnimationTasks.remove(0);
                    boolean state = task.tick();
                    if (state) {
                        particleAnimationTasks.add(task);
                    }
                }
            }, 100);
        }
    }

}
