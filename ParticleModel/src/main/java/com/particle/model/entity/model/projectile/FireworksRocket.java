package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class FireworksRocket extends ProjectileEntity {
    @Override
    public String getName() {
        return "fireworks_rocket";
    }

    @Override
    public int getNetworkId() {
        return FIREWORK_ROCKET;
    }

    @Override
    public String getActorType() {
        return EntityType.FIREWORKS_ROCKET.actorType();
    }

    @Override
    protected void onInit() {
    }
}
