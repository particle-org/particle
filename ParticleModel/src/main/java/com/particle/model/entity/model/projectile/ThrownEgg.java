package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class ThrownEgg extends ProjectileEntity {
    @Override
    public String getName() {
        return "egg";
    }

    @Override
    public int getNetworkId() {
        return ProjectileEntity.EGG;
    }

    @Override
    public String getActorType() {
        return EntityType.EGG.actorType();
    }

    @Override
    protected void onInit() {

    }
}
