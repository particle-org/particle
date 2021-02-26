package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class DragonFireball extends ProjectileEntity {
    @Override
    public String getName() {
        return "dragon_fireball";
    }

    @Override
    public int getNetworkId() {
        return DRAGON_FIREBALL;
    }

    @Override
    public String getActorType() {
        return EntityType.DRAGON_FIREBALL.actorType();
    }

    @Override
    protected void onInit() {

    }
}
