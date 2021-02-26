package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class SmallFireball extends ProjectileEntity {
    @Override
    public String getName() {
        return "small_fireball";
    }

    @Override
    public int getNetworkId() {
        return SMALL_FIREBALL;
    }

    @Override
    public String getActorType() {
        return EntityType.SMALL_FIREBALL.actorType();
    }

    @Override
    protected void onInit() {

    }
}
