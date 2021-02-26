package com.particle.game.item;

import com.particle.core.ecs.module.BehaviorModule;

public class PickableModule extends BehaviorModule {
    private long pickupCooling = 20;
    private long binderEntity = -1;

    public void cooldown() {
        if (this.pickupCooling > 0) {
            this.pickupCooling--;
        }
    }

    public void setPickupCooling(long pickupCooling) {
        this.pickupCooling = pickupCooling;
    }

    public long getPickupCooling() {
        return pickupCooling;
    }

    public long getBinderEntity() {
        return binderEntity;
    }

    public void setBinderEntity(long binderEntity) {
        this.binderEntity = binderEntity;
    }
}
