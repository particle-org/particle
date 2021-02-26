package com.particle.model.effect;

public enum EffectBaseType {
    SPEED(1, "speed", 124, 175, 198),
    SLOWNESS(2, "slowness", 90, 108, 129),
    HASTE(3, "haste", 217, 192, 67),
    FATIGUE(4, "mining_fatigue", 74, 66, 23),
    STRENGTH(5, "strength", 147, 36, 35),
    HEALING(6, "instant_health", 248, 36, 35),
    HARMING(7, "instant_damage", 67, 10, 9),
    LEAPING(8, "jump_boost", 34, 255, 76),
    NAUSEA(9, "nausea", 85, 29, 74),
    REGENERATION(10, "regeneration", 205, 92, 171),
    DAMAGE_RESISTANCE(11, "resistance", 153, 69, 58),
    FIRE_RESISTANCE(12, "fire_resistance", 228, 154, 58),
    WATER_BREATHING(13, "water_breathing", 46, 82, 153),
    INVISIBILITY(14, "invisibility", 127, 131, 146),
    BLINDNESS(15, "blindness", 191, 192, 192),
    NIGHT_VISION(16, "night_vision", 0, 0, 139),
    HUNGER(17, "hunger", 46, 139, 87),
    WEAKNESS(18, "weakness", 72, 77, 72),
    POISON(19, "poison", 78, 147, 49),
    WITHER(20, "wither", 53, 42, 39),
    HEALTH_BOOST(21, "health_boost", 248, 125, 35),
    ABSORPTION(22, "absorption", 36, 107, 251),
    SATURATION(23, "saturation", 255, 0, 255);

    private int id;
    private int color;
    private String name;

    EffectBaseType(int id, String name, int r, int g, int b) {
        this.id = id;
        this.name = name;
        this.color = ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}