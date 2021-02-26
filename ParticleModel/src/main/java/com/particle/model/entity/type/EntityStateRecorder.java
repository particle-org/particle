package com.particle.model.entity.type;

public class EntityStateRecorder {

    private String name;
    private int level;
    private long lastUpdatedTimestamp;
    private long updateInterval = 500;
    private long enableTimestamp;
    private long timeToLive = -1;

    // 叠加次数，在bindstate中使用
    private int enabledCount = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public long getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(long updateInterval) {
        this.updateInterval = updateInterval;
    }

    public long getEnableTimestamp() {
        return enableTimestamp;
    }

    public void setEnableTimestamp(long enableTimestamp) {
        this.enableTimestamp = enableTimestamp;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public int getEnabledCount() {
        return enabledCount;
    }

    public void setEnabledCount(int enabledCount) {
        this.enabledCount = enabledCount;
    }
}
