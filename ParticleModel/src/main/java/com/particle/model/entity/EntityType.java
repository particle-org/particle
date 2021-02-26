package com.particle.model.entity;

import java.util.HashMap;
import java.util.Map;

public enum EntityType {
    UNDEFINED(1, ":"),

    // Passive and neutral mobs
    CHICKEN(10, "minecraft:chicken"),
    COW(11, "minecraft:cow"),
    PIG(12, "minecraft:pig"),
    SHEEP(13, "minecraft:sheep"),
    WOLF(14, "minecraft:wolf"),
    VILLAGER(15, "minecraft:villager"),
    MOOSHROOM(16, "minecraft:mooshroom"),
    SQUID(17, "minecraft:squid"),
    RABBIT(18, "minecraft:rabbit"),
    BAT(19, "minecraft:bat"),
    IRON_GOLEM(20, "minecraft:iron_golem"),
    SNOW_GOLEM(21, "minecraft:snow_golem"),
    OCELOT(22, "minecraft:ocelot"),
    HORSE(23, "minecraft:horse"),
    DONKEY(24, "minecraft:donkey"),
    MULE(25, "minecraft:mule"),
    SKELETON_HORSE(26, "minecraft:skeleton_horse"),
    ZOMBIE_HORSE(27, "minecraft:zombie_horse"),
    POLAR_BEAR(28, "minecraft:polar_bear"),
    LLAMA(29, "minecraft:llama"),
    PARROT(30, "minecraft:parrot"),
    DOLPHIN(31, "minecraft:dolphin"),
    ZOMBIE(32, "minecraft:zombie"),
    CREEPER(33, "minecraft:creeper"),
    SKELETON(34, "minecraft:skeleton"),
    SPIDER(35, "minecraft:spider"),
    ZOMBIE_PIGMAN(36, "minecraft:zombie_pigman"),
    SLIME(37, "minecraft:slime"),
    ENDERMAN(38, "minecraft:enderman"),
    SILVERFISH(39, "minecraft:silverfish"),
    CAVE_SPIDER(40, "minecraft:cave_spider"),
    GHAST(41, "minecraft:ghast"),
    MAGMA_CUBE(42, "minecraft:magma_cube"),
    BLAZE(43, "minecraft:blaze"),
    ZOMBIE_VILLAGER(44, "minecraft:zombie_villager"),
    WITCH(45, "minecraft:witch"),
    STRAY(46, "minecraft:stray"),
    HUSK(47, "minecraft:husk"),
    WITHER_SKELETON(48, "minecraft:wither_skeleton"),
    GUARDIAN(49, "minecraft:guardian"),
    ELDER_GUARDIAN(50, "minecraft:elder_guardian"),
    NPC(51, "minecraft:npc"),
    WITHER(52, "minecraft:wither"),
    ENDER_DRAGON(53, "minecraft:ender_dragon"),
    SHULKER(54, "minecraft:shulker"),
    ENDERMITE(55, "minecraft:endermite"),
    AGENT(56, "minecraft:agent"),
    VINDICATOR(57, "minecraft:vindicator"),
    PHANTOM(58, "minecraft:phantom"),
    RAVAGER(58, "minecraft:ravager"),
    ARMOR_STAND(61, "minecraft:armor_stand"),
    TRIPOD_CAMERA(62, "minecraft:tripod_camera"),
    PLAYER(63, "minecraft:player"),
    ITEM(64, "minecraft:item"),
    TNT(65, "minecraft:tnt"),
    FALLING_BLOCK(66, "minecraft:falling_block"),
    MOVING_BLOCK(67, ""),
    XP_BOTTLE(68, "minecraft:xp_bottle"),
    XP_ORB(69, "minecraft:xp_orb"),
    EYE_OF_ENDER_SIGNAL(70, "minecraft:eye_of_ender_signal"),
    ENDER_CRYSTAL(71, "minecraft:ender_crystal"),
    FIREWORKS_ROCKET(72, "minecraft:fireworks_rocket"),
    THROWN_TRIDENT(73, "minecraft:thrown_trident"),
    TURTLE(74, "minecraft:turtle"),
    CAT(75, "minecraft:cat"),
    SHULKER_BULLET(76, "minecraft:shulker_bullet"),
    FISHING_HOOK(77, "minecraft:fishing_hook"),
    CHALKBOARD(78, ""),
    DRAGON_FIREBALL(79, "minecraft:dragon_fireball"),
    ARROW(80, "minecraft:arrow"),
    SNOWBALL(81, "minecraft:snowball"),
    EGG(82, "minecraft:egg"),
    PAINTING(83, "minecraft:painting"),
    MINECART(84, "minecraft:minecart"),
    FIREBALL(85, "minecraft:fireball"),
    SPLASH_POTION(86, "minecraft:splash_potion"),
    ENDER_PEARL(87, "minecraft:ender_pearl"),
    LEASH_KNOT(88, "minecraft:leash_knot"),
    WITHER_SKULL(89, "minecraft:wither_skull"),
    BOAT(90, "minecraft:boat"),
    WITHER_SKULL_DANGEROUS(91, "minecraft:wither_skull_dangerous"),
    LIGHTNING_BOLT(93, "minecraft:lightning_bolt"),
    SMALL_FIREBALL(94, "minecraft:small_fireball"),
    AREA_EFFECT_CLOUD(95, "minecraft:area_effect_cloud"),
    HOPPER_MINECART(96, "minecraft:hopper_minecart"),
    TNT_MINECART(97, "minecraft:tnt_minecart"),
    CHEST_MINECART(98, "minecraft:chest_minecart"),
    COMMAND_BLOCK_MINECART(100, "minecraft:command_block_minecart"),
    LINGERING_POTION(101, "minecraft:lingering_potion"),
    LLAMA_SPIT(102, "minecraft:llama_spit"),
    EVOCATION_FANG(103, "minecraft:evocation_fang"),
    EVOCATION_ILLAGER(104, "minecraft:evocation_illager"),
    VEX(105, "minecraft:vex"),
    ICE_BOMB(106, "minecraft:ice_bomb"),
    BALLOON(107, "minecraft:balloon"),
    PUFFERFISH(108, "minecraft:pufferfish"),
    SALMON(109, "minecraft:salmon"),
    DROWNED(110, "minecraft:drowned"),
    TROPICALFISH(111, "minecraft:tropicalfish"),
    FISH(112, "minecraft:fish"),
    PANDA(113, "minecraft:panda"),
    PILLAGER(114, "minecraft:pillager"),
    ELDER_GUARDIAN_GHOST(120, "minecraft:elder_guardian_ghost"),
    FOX(121, "minecraft:fox"),
    BEE(121, "minecraft:bee"),
    MAX_ENTITY_ID(127, "");

    private int type;

    private String actorType;

    private static Map<Integer, EntityType> allEntityTypeIs = new HashMap<>();
    private static Map<String, EntityType> allEntityTypeSs = new HashMap<>();

    static {
        for (EntityType entityType : EntityType.values()) {
            allEntityTypeIs.put(entityType.type, entityType);
            allEntityTypeSs.put(entityType.actorType, entityType);
        }
    }

    EntityType(int type, String actorType) {
        this.type = type;
        this.actorType = actorType;
    }

    public int type() {
        return this.type;
    }

    public String actorType() {
        return actorType;
    }

    public static EntityType fromValue(int value) {
        return allEntityTypeIs.get(value);
    }

    public static EntityType fromValue(String value) {
        return allEntityTypeSs.get(value);
    }
}
