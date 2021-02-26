package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class Arrow extends ProjectileEntity {
    @Override
    public String getName() {
        return "arrow";
    }

    @Override
    public int getNetworkId() {
        return ARROW;
    }

    @Override
    public String getActorType() {
        return EntityType.ARROW.actorType();
    }

    @Override
    public void onInit() {
    }
}
