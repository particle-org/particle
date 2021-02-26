package com.particle.model.entity.model.projectile;

import com.particle.model.entity.EntityType;

public class LingeringPotion extends ProjectileEntity {
    @Override
    public String getName() {
        return "lingering_potion";
    }

    @Override
    public int getNetworkId() {
        return ProjectileEntity.LINGERING_POTION;
    }

    @Override
    public String getActorType() {
        return EntityType.LINGERING_POTION.actorType();
    }

    @Override
    protected void onInit() {

    }
}
