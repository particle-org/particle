package com.particle.game.entity.attack.processor;

import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;

import java.util.function.Consumer;

public interface IHitEntityProcessor {
    Consumer<Entity> getColliderEntityCallback(Entity damager, ItemStack weapon, Entity projectile);

    Consumer<Vector3f> getColliderBlockCallback(Entity damager, ItemStack weapon, Entity projectile);
}
