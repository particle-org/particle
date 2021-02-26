package com.particle.model.entity.model.projectile;

import com.particle.model.entity.LivingEntity;
import com.particle.model.entity.id.IDAllocation;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ProjectileEntity extends LivingEntity {

    private static AtomicInteger index = new AtomicInteger();

    public static final int EYE_OF_ENDER = 70;
    public static final int FIREWORK_ROCKET = 72;
    public static final int TRIDENT = 73;
    public static final int SHULKER_BULLET = 76;
    public static final int DRAGON_FIREBALL = 79;
    public static final int ARROW = 80;
    public static final int SNOWBALL = 81;
    public static final int EGG = 82;
    public static final int LARGE_FIREBALL = 85;
    public static final int SPLASH_POTION = 86;
    public static final int ENDER_PEARL = 87;
    public static final int SMALL_FIREBALL = 94;
    public static final int LINGERING_POTION = 101;

    public abstract String getName();

    public abstract int getNetworkId();

    public abstract String getActorType();

    protected long generateRuntimeId() {
        return IDAllocation.PROJECTILE_ENTITY_BASE_ID + index.getAndIncrement();
    }
}
