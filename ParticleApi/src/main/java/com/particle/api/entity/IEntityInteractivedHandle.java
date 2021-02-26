package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;

@FunctionalInterface
public interface IEntityInteractivedHandle {
    void handle(Entity interactor, ItemStack itemOnHand);
}
