package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;

public interface IEntityRemoteAttackServiceApi {
    void initEntityRemoteAttackComponent(Entity entity, long attackInterval);

    void initEntityRemoteAttackComponent(Entity entity, long attackInterval, float damageRate);

    Entity projectileShoot(Entity entity, ItemStack weapon, Vector3f motion);

    void commonProjectileEntityShoot(Entity sourceEntity, Entity projectileEntity, Vector3f motion);

    void projectileEntityShoot(Entity entity, Entity projectileEntity, Vector3f motion);
}
