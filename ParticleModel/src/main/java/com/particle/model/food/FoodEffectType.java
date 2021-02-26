package com.particle.model.food;

import com.particle.model.effect.EffectBaseData;
import com.particle.model.effect.EffectBaseType;

import java.util.Random;

public enum FoodEffectType {
    GOLD_APPLE_REGERATION(EffectBaseType.REGENERATION, 2, 5, 1f),
    GOLD_APPLE_ABSORPTION(EffectBaseType.ABSORPTION, 4, 120, 1f),
    ENCHANT_APPLE_REGERATION(EffectBaseType.REGENERATION, 2, 20, 1f),
    ENCHANT_APPLE_ABSORPTION(EffectBaseType.ABSORPTION, 4, 120, 1f),
    ENCHANT_APPLE_DAMAGE_RESISTANCE(EffectBaseType.DAMAGE_RESISTANCE, 1, 300, 1f),
    ENCHANT_APPLE_FIRE_RESISTANCE(EffectBaseType.FIRE_RESISTANCE, 1, 300, 1f),
    POTATO_POISON(EffectBaseType.POISON, 1, 4, 0.6f),
    PUFFERFISH_HUNGER(EffectBaseType.HUNGER, 3, 15, 1f),
    PUFFERFISH_NAUSEA(EffectBaseType.NAUSEA, 2, 15, 1f),
    PUFFERFISH_POISON(EffectBaseType.POISON, 4, 60, 1f),
    RAW_CHICKEN_HUNGER(EffectBaseType.HUNGER, 1, 30, 0.3f),
    ROTTEN_FLESH_HUNGER(EffectBaseType.HUNGER, 1, 30, 0.8f),
    SPIDER_EYE_POISON(EffectBaseType.POISON, 1, 4, 1f);


    /**
     * 状态效果
     */
    private EffectBaseData effectBaseData;

    /**
     * 概率
     */
    private float scale;

    FoodEffectType(EffectBaseType type, int level, long duration, float scale) {
        this.effectBaseData = new EffectBaseData(type, level, duration);
        this.scale = scale;
    }

    public EffectBaseData getEffectBaseData() {
        return effectBaseData;
    }

    /**
     * 根据概率判断是否有效
     *
     * @return
     */
    public boolean isValid() {
        if (this.scale < 1f) {
            float value = new Random().nextFloat();
            if (value < this.scale) {
                return true;
            }
            return false;
        }
        return true;
    }
}
