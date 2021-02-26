package com.particle.executor.model;

public class RepeatingTask extends CancellableTask {
    private long lastExecutedTime;
    private long interval;

    public RepeatingTask(String name, long interval) {
        super(name, null);
        this.interval = interval;
    }

    public long getLastExecutedTime() {
        return lastExecutedTime;
    }

    public void setLastExecutedTime(long lastExecutedTime) {
        this.lastExecutedTime = lastExecutedTime;
    }

    public long getExecutedTimestamp() {
        return this.lastExecutedTime + this.interval;
    }
}
