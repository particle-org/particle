package com.particle.model.entity.id;

public class IDAllocation {
    public static long PLAYER_BASE_ID = 1L << 63;
    public static long ENTITY_BASE_ID = 1024L << 32;
    public static long ITEM_ENTITY_BASE_ID = 1356L << 32;
    public static long PROJECTILE_ENTITY_BASE_ID = 2048L << 32;
    public static long NPC_BASE_ID = 2560L << 32;
    public static long HD_BASE_ID = 3072L << 32;
    public static long MONSTER_BASE_ID = 4096L << 32;
}
