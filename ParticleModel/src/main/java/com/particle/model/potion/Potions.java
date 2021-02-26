package com.particle.model.potion;

import com.particle.model.effect.EffectBaseData;
import com.particle.model.effect.EffectBaseType;

import java.util.HashMap;
import java.util.Map;

public enum Potions {
    WATER_BOTTLE(0, PotionType.WATER_BOTTLE, null, 1, 0, false, false),
    MUNDANE(1, PotionType.MUNDANE, null, 1, 180, false, false),
    LONG_MUNDANE(2, PotionType.MUNDANE, null, 2, 480, false, false),
    THICK(3, PotionType.THICK, null, 1, 180, false, false),
    AWKWARD(4, PotionType.AWKWARD, null, 1, 180, false, false),
    NIGHT_VISION(5, PotionType.NIGHT_VISION, EffectBaseType.NIGHT_VISION, 1, 180, false, false),
    LONG_NIGHT_VISION(6, PotionType.NIGHT_VISION, EffectBaseType.NIGHT_VISION, 2, 480, false, false),
    INVISIBILITY(7, PotionType.INVISIBILITY, EffectBaseType.INVISIBILITY, 1, 180, false, false),
    LONG_INVISIBILITY(8, PotionType.INVISIBILITY, EffectBaseType.INVISIBILITY, 2, 480, false, false),
    LEAPING(9, PotionType.LEAPING, EffectBaseType.LEAPING, 1, 180, false, false),
    LONG_LEAPING(10, PotionType.LEAPING, EffectBaseType.LEAPING, 2, 480, false, false),
    ENHANCE_LEAPING(11, PotionType.LEAPING, EffectBaseType.LEAPING, 3, 90, false, false),
    FIRE_RESISTANCE(12, PotionType.FIRE_RESISTANCE, EffectBaseType.FIRE_RESISTANCE, 1, 180, false, false),
    LONG_FIRE_RESISTANCE(13, PotionType.FIRE_RESISTANCE, EffectBaseType.FIRE_RESISTANCE, 2, 480, false, false),
    SWIFTNESS(14, PotionType.SWIFTNESS, EffectBaseType.HASTE, 1, 180, false, false),
    LONG_SWIFTNESS(15, PotionType.SWIFTNESS, EffectBaseType.HASTE, 2, 480, false, false),
    ENHANCE_SWIFTNESS(16, PotionType.SWIFTNESS, EffectBaseType.HASTE, 3, 90, false, false),
    SLOWNESS(17, PotionType.SLOWNESS, EffectBaseType.SLOWNESS, 1, 90, false, false),
    LONG_SLOWNESS(18, PotionType.SLOWNESS, EffectBaseType.SLOWNESS, 1, 240, false, false),
    WATER_BREATHING(19, PotionType.WATER_BREATHING, EffectBaseType.WATER_BREATHING, 1, 180, false, false),
    LONG_WATER_BREATHING(20, PotionType.WATER_BREATHING, EffectBaseType.WATER_BREATHING, 2, 480, false, false),
    HEALING(21, PotionType.HEALING, null, 1, 180, false, false),
    ENHANCE_HEALING(22, PotionType.HEALING, null, 2, 90, false, false),
    HARMING(23, PotionType.HARMING, EffectBaseType.HARMING, 1, 180, false, false),
    ENHANCE_HARMING(24, PotionType.HARMING, EffectBaseType.HARMING, 2, 90, false, false),
    POISON(25, PotionType.POISON, EffectBaseType.POISON, 1, 180, false, false),
    LONG_POISON(26, PotionType.POISON, EffectBaseType.POISON, 2, 480, false, false),
    ENHANCE_POISON(27, PotionType.POISON, EffectBaseType.POISON, 3, 90, false, false),
    REGENERATION(28, PotionType.REGENERATION, EffectBaseType.REGENERATION, 1, 180, false, false),
    LONG_REGENERATION(29, PotionType.REGENERATION, EffectBaseType.REGENERATION, 2, 480, false, false),
    ENHANCE_REGENERATION(30, PotionType.REGENERATION, EffectBaseType.REGENERATION, 3, 90, false, false),
    STRENGTH(31, PotionType.STRENGTH, EffectBaseType.STRENGTH, 1, 180, false, false),
    LONG_STRENGTH(32, PotionType.STRENGTH, EffectBaseType.STRENGTH, 2, 480, false, false),
    ENHANCE_STRENGTH(33, PotionType.STRENGTH, EffectBaseType.STRENGTH, 3, 90, false, false),
    WEAKNESS(34, PotionType.WEAKNESS, EffectBaseType.WEAKNESS, 1, 180, false, false),
    LONG_WEAKNESS(35, PotionType.WEAKNESS, EffectBaseType.WEAKNESS, 2, 480, false, false);

    private int data;
    private PotionType type;
    private EffectBaseData effectBaseData;
    private boolean longTime;
    private boolean isBad;

    private static final Map<Integer, Potions> allEffectById = new HashMap<>();

    static {
        Potions[] potions = Potions.values();
        for (Potions potion : potions) {
            allEffectById.put(potion.data, potion);
        }
    }

    public static Potions fromId(int id) {
        return allEffectById.get(id);
    }

    Potions(int data, PotionType type, EffectBaseType effectBaseType, int level, int duration, boolean longTime, boolean isBad) {
        this.data = data;
        this.type = type;
        this.effectBaseData = new EffectBaseData(effectBaseType, level, duration);
        this.longTime = longTime;
        this.isBad = isBad;
    }

    public int getData() {
        return data;
    }

    public PotionType getType() {
        return type;
    }

    public long getDuration() {
        return this.effectBaseData.getDuration();
    }

    public EffectBaseData getEffectBaseData() {
        return effectBaseData;
    }
}
