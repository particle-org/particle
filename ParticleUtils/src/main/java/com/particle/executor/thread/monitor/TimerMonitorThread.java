package com.particle.executor.thread.monitor;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;
import com.particle.executor.thread.IScheduleThread;

import java.util.List;
import java.util.concurrent.Future;

public class TimerMonitorThread implements IScheduleThread {

    private IScheduleThread scheduleThread;

    public TimerMonitorThread(IScheduleThread scheduleThread) {
        this.scheduleThread = scheduleThread;
    }

    @Override
    public Future scheduleRealtimeTask(String name, ExecutableTask task, long ttl) {
        return this.scheduleThread.scheduleRealtimeTask(name, new MonitorTask(name, task), ttl);
    }

    @Override
    public Future scheduleRealtimeTask(String name, ExecutableTask task, ExecutableCallback callback, long ttl) {
        return this.scheduleThread.scheduleRealtimeTask(name, new MonitorTask(name, task), callback, ttl);
    }

    @Override
    public Future scheduleSimpleTask(String name, ExecutableTask task) {
        return this.scheduleThread.scheduleSimpleTask(name, new MonitorTask(name, task));
    }

    @Override
    public Future scheduleSimpleTask(String name, ExecutableTask task, ExecutableCallback callback) {
        return this.scheduleThread.scheduleSimpleTask(name, new MonitorTask(name, task), callback);
    }

    @Override
    public Future scheduleDelayTask(String name, ExecutableTask task, long time) {
        return this.scheduleThread.scheduleDelayTask(name, new MonitorTask(name, time, task), time);
    }

    @Override
    public Future scheduleDelayTask(String name, ExecutableTask task, ExecutableCallback callback, long time) {
        return this.scheduleThread.scheduleDelayTask(name, new MonitorTask(name, time, task), callback, time);
    }

    @Override
    public Future scheduleRepeatingTask(String name, ExecutableTask task, long interval) {
        return this.scheduleThread.scheduleRepeatingTask(name, new MonitorTask(name, interval, task), interval);
    }

    @Override
    public Future scheduleRepeatingTask(String name, ExecutableTask task, long interval, boolean keeplive) {
        return this.scheduleThread.scheduleRepeatingTask(name, new MonitorTask(name, interval, task), interval, keeplive);
    }

    @Override
    public List<String> getScheduledTaskList() {
        return this.scheduleThread.getScheduledTaskList();
    }

    @Override
    public List<String> getRepeatingTaskList() {
        return this.scheduleThread.getRepeatingTaskList();
    }

    @Override
    public long getScheduledTasksAmount() {
        return this.scheduleThread.getScheduledTasksAmount();
    }

    @Override
    public long getRepeatingTasksAmount() {
        return this.scheduleThread.getRepeatingTasksAmount();
    }

    @Override
    public long getId() {
        return this.scheduleThread.getId();
    }

    @Override
    public String getName() {
        return this.scheduleThread.getName();
    }

    @Override
    public void setName(String name) {
        this.scheduleThread.setName(name);
    }

    @Override
    public void execute(Runnable runnable) {
        this.scheduleThread.execute(runnable);
    }

    @Override
    public void terminate() {
        this.scheduleThread.terminate();
    }

    @Override
    public long getExecutingTaskAmount() {
        return this.scheduleThread.getExecutingTaskAmount();
    }

    @Override
    public long getExecutedTaskAmount() {
        return this.scheduleThread.getExecutedTaskAmount();
    }

    @Override
    public long getSubmitedTaskAmount() {
        return this.scheduleThread.getSubmitedTaskAmount();
    }
}
