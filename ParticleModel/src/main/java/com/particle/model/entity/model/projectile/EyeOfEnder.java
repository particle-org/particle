package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class EyeOfEnder extends ProjectileEntity {
    @Override
    public String getName() {
        return "eye_of_ender_signal";
    }

    @Override
    public int getNetworkId() {
        return EYE_OF_ENDER;
    }

    @Override
    public String getActorType() {
        return EntityType.EYE_OF_ENDER_SIGNAL.actorType();
    }

    @Override
    protected void onInit() {

    }
}
