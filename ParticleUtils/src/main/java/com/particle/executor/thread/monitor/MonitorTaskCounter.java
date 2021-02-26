package com.particle.executor.thread.monitor;

public class MonitorTaskCounter {

    private String name;
    private long submitCount;
    private MonitorTaskCounteUnit waitingTime = new MonitorTaskCounteUnit();
    private MonitorTaskCounteUnit runningTime = new MonitorTaskCounteUnit();

    public MonitorTaskCounter(String name) {
        this.name = name;
    }

    public void recorder(MonitorTask monitorTask) {
        this.submitCount++;
        this.waitingTime.addData(monitorTask.getWaitingTime());
        this.runningTime.addData(monitorTask.getRunningTime());
    }

    public String getName() {
        return name;
    }

    public long getSubmitCount() {
        return submitCount;
    }

    public MonitorTaskCounteUnit getWaitingTime() {
        return waitingTime;
    }

    public MonitorTaskCounteUnit getRunningTime() {
        return runningTime;
    }
}
