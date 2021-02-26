package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class LargeFireball extends ProjectileEntity {
    @Override
    public String getName() {
        return "large_fireball";
    }

    @Override
    public int getNetworkId() {
        return LARGE_FIREBALL;
    }

    @Override
    public String getActorType() {
        return EntityType.FIREBALL.actorType();
    }

    @Override
    protected void onInit() {

    }
}
