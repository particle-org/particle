package com.particle.model.effect;

public class EffectRecorder {
    private long playerId;
    private int taskId;
    private EffectBaseData effectBaseData;
    private long startTime;

    public EffectRecorder(long playerId, int taskId, EffectBaseData effectBaseData, long startTime) {
        this.playerId = playerId;
        this.taskId = taskId;
        this.effectBaseData = effectBaseData;
        this.startTime = startTime;
    }

    public long getPlayerId() {
        return playerId;
    }

    public int getTaskId() {
        return taskId;
    }

    public EffectBaseType getEffectType() {
        return effectBaseData.getEffectType();
    }

    public long getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return effectBaseData.getDuration();
    }

    public int getLevel() {
        return effectBaseData.getLevel();
    }
}
