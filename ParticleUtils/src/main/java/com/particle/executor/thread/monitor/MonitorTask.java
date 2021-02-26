package com.particle.executor.thread.monitor;

import com.particle.executor.api.ExecutableTask;

public class MonitorTask implements ExecutableTask {

    private String name;
    private ExecutableTask task;
    private long submitTimestamp;
    private long lastSubmitTimestamp;
    private long executingTimestamp;
    private long executedTimestamp;
    private long delay = 0;

    public MonitorTask(String name, ExecutableTask task) {
        this.name = name;
        this.task = task;

        this.submitTimestamp = System.nanoTime();
    }

    public MonitorTask(String name, long delay, ExecutableTask task) {
        this.name = name;
        this.task = task;
        this.delay = delay * 1000000;

        this.lastSubmitTimestamp = this.submitTimestamp = System.nanoTime();
    }

    public long getWaitingTime() {
        return this.executingTimestamp - (this.lastSubmitTimestamp + this.delay);
    }

    public long getRunningTime() {
        return this.executedTimestamp - this.executingTimestamp;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        this.executingTimestamp = System.nanoTime();

        this.task.run();

        this.executedTimestamp = System.nanoTime();

        this.lastSubmitTimestamp = this.submitTimestamp;
        this.submitTimestamp = this.executedTimestamp;

        MonitorTaskAnalyse.getInstance().analyse(Thread.currentThread().getName(), this);
    }
}