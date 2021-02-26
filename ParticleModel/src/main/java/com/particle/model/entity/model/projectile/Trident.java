package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class Trident extends ProjectileEntity {
    @Override
    public String getName() {
        return "thrown_trident";
    }

    @Override
    public int getNetworkId() {
        return ProjectileEntity.TRIDENT;
    }

    @Override
    public String getActorType() {
        return EntityType.THROWN_TRIDENT.actorType();
    }

    @Override
    protected void onInit() {
    }
}
