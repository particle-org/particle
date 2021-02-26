package com.particle.game.world.physical.modules;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.game.world.physical.collider.PointCollider;
import com.particle.game.world.physical.module.ColliderDetectAlgorithm;

public class PointColliderModule extends BehaviorModule {

    private PointCollider pointCollider;

    private ColliderDetectAlgorithm detectAlgorithm = ColliderDetectAlgorithm.SAMPLING;

    public PointCollider getPointCollider() {
        return pointCollider;
    }

    public void setPointCollider(PointCollider pointCollider) {
        this.pointCollider = pointCollider;
    }

    public ColliderDetectAlgorithm getDetectAlgorithm() {
        return detectAlgorithm;
    }

    public void setDetectAlgorithm(ColliderDetectAlgorithm detectAlgorithm) {
        this.detectAlgorithm = detectAlgorithm;
    }
}
