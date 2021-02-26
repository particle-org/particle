package com.particle.model.entity.attribute;

public enum EntityAttributeType {
    ABSORPTION("minecraft:absorption"),
    SATURATION("minecraft:player.saturation"),
    EXHAUSTION("minecraft:player.exhaustion"),
    KNOCK_BACK_RESISTANCE("minecraft:knockback_resistance"),
    HEALTH("minecraft:health"),
    MOVEMENT_SPEED("minecraft:movement"),
    FOLLOW_RANGE("minecraft:follow_range"),
    HUNGER("minecraft:player.hunger"),
    ATTACK_DAMAGE("minecraft:attack_damage"),
    EXPERIENCE_LEVEL("minecraft:player.level"),
    EXPERIENCE("minecraft:player.experience");

    private String name;

    private EntityAttributeType(String name) {
        this.name = name;
    }

    public String value() {
        return this.name;
    }

    public static EntityAttributeType fromName(String name) {
        switch (name) {
            case "minecraft:absorption":
                return ABSORPTION;
            case "minecraft:player.saturation":
                return SATURATION;
            case "minecraft:player.exhaustion":
                return EXHAUSTION;
            case "minecraft:knockback_resistance":
                return KNOCK_BACK_RESISTANCE;
            case "minecraft:health":
                return HEALTH;
            case "minecraft:movement":
                return MOVEMENT_SPEED;
            case "minecraft:follow_range":
                return FOLLOW_RANGE;
            case "minecraft:player.hunger":
                return HUNGER;
            case "minecraft:attack_damage":
                return ATTACK_DAMAGE;
            case "minecraft:player.level":
                return EXPERIENCE_LEVEL;
            case "minecraft:player.experience":
                return EXPERIENCE;
        }

        return null;
    }
}
