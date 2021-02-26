package com.particle.game.entity.link;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector2f;

public class EntityPassengerModule extends BehaviorModule {

    private Entity vehicle;

    private Vector2f aim;

    public Entity getVehicle() {
        return vehicle;
    }

    public void setVehicle(Entity vehicle) {
        this.vehicle = vehicle;
    }

    public Vector2f getAim() {
        return aim;
    }

    public void setAim(Vector2f aim) {
        this.aim = aim;
    }

}
