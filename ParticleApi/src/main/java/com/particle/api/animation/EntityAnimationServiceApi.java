package com.particle.api.animation;

import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

public interface EntityAnimationServiceApi {
    public void hurt(Entity entity);

    public void exploding(Entity entity, boolean isExploding);

    public long throwItem(Entity sender, ItemStack itemStack, Vector3f position, Vector3f motion, long ttl);

    public void throwItem(Entity sender, long itemId, ItemStack itemStack, Vector3f position, Vector3f motion, long ttl);

    public long throwItemFor(Player receiver, ItemStack itemStack, Vector3f position, Vector3f motion, long ttl);

    public void throwItemFor(Player receiver, long itemId, ItemStack itemStack, Vector3f position, Vector3f motion, long ttl);

    public void removeThrowItem(Entity sender, long itemId);

    public void sendAnimation(Entity entity, int eventId);
}
