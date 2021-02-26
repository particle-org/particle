package com.particle.game.entity.link;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.entity.Entity;

public class MountModule extends BehaviorModule {
    private Entity own;

    public Entity getOwn() {
        return own;
    }

    public void setOwn(Entity own) {
        this.own = own;
    }
}
