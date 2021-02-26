package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class EnderPearl extends ProjectileEntity {
    @Override
    public String getName() {
        return "ender_pearl";
    }

    @Override
    public int getNetworkId() {
        return ENDER_PEARL;
    }

    @Override
    public String getActorType() {
        return EntityType.ENDER_PEARL.actorType();
    }

    @Override
    protected void onInit() {

    }
}
