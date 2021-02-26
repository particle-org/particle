package com.particle.model.effect;

public class EffectBaseData {
    private EffectBaseType effectBaseType;
    private int level;
    private long duration;

    public EffectBaseData(EffectBaseType effectBaseType, int level, long duration) {
        this.effectBaseType = effectBaseType;
        this.level = level;
        this.duration = duration;
    }

    public EffectBaseType getEffectType() {
        return effectBaseType;
    }

    public long getDuration() {
        return duration;
    }

    public int getLevel() {
        return level;
    }
}
