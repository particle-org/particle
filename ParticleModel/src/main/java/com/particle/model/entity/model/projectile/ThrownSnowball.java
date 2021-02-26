package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class ThrownSnowball extends ProjectileEntity {
    @Override
    public String getName() {
        return "snowball";
    }

    @Override
    public int getNetworkId() {
        return ProjectileEntity.SNOWBALL;
    }

    @Override
    public String getActorType() {
        return EntityType.SNOWBALL.actorType();
    }

    @Override
    protected void onInit() {

    }
}
