package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class ShulkerBullet extends ProjectileEntity {
    @Override
    public String getName() {
        return "shulker_bullet";
    }

    @Override
    public int getNetworkId() {
        return ProjectileEntity.SHULKER_BULLET;
    }

    @Override
    public String getActorType() {
        return EntityType.SHULKER_BULLET.actorType();
    }

    @Override
    protected void onInit() {

    }
}
