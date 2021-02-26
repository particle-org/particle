package com.particle.executor.thread.monitor;

public class MonitorTaskCounteUnit {
    private long count;
    private long min;
    private long max;

    public void addData(long data) {
        this.count += data;

        if (this.min > data) {
            this.min = data;
        }

        if (this.max < data) {
            this.max = data;
        }
    }

    public long getCount() {
        return count;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }
}
