package com.particle.game.world.physical.modules;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.entity.Entity;

public class RigibodyModule extends BehaviorModule {

    private float gravity = 9.8f / 20;

    private boolean kinematic = false;
    private boolean enableGravity = true;

    private Entity linkedEntity;

    public boolean isKinematic() {
        return kinematic;
    }

    public void setKinematic(boolean kinematic) {
        this.kinematic = kinematic;
    }

    public Entity getLinkedEntity() {
        return linkedEntity;
    }

    public void setLinkedEntity(Entity linkedEntity) {
        this.linkedEntity = linkedEntity;
    }

    public boolean isEnableGravity() {
        return enableGravity;
    }

    public void setEnableGravity(boolean enableGravity) {
        this.enableGravity = enableGravity;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

}
