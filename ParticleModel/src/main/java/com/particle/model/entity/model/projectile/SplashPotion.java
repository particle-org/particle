package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class SplashPotion extends ProjectileEntity {
    @Override
    public String getName() {
        return "splash_potion";
    }

    @Override
    public int getNetworkId() {
        return EntityType.SPLASH_POTION.type();
    }

    @Override
    public String getActorType() {
        return EntityType.SPLASH_POTION.actorType();
    }

    @Override
    protected void onInit() {

    }
}
