package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;

public interface IProjectileEntityServiceApi {
    Entity createEntity(String id, ItemStack weapon, Vector3f position, Vector3f motion);
}
