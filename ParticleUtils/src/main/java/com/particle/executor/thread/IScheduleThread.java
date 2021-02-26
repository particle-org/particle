package com.particle.executor.thread;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;

import java.util.List;
import java.util.concurrent.Future;

public interface IScheduleThread extends IWorkThread {

    default Future scheduleRealtimeTask(String name, ExecutableTask task, long ttl) {
        return this.scheduleRealtimeTask(name, task, null, ttl);
    }

    Future scheduleRealtimeTask(String name, ExecutableTask task, ExecutableCallback callback, long ttl);

    default Future scheduleSerialSimpleTask(String name, String identified, ExecutableTask task) {
        return scheduleSimpleTask(name, task);
    }

    default Future scheduleSimpleTask(String name, ExecutableTask task) {
        return this.scheduleSimpleTask(name, task, null);
    }

    Future scheduleSimpleTask(String name, ExecutableTask task, ExecutableCallback callback);

    default Future scheduleDelayTask(String name, ExecutableTask task, long time) {
        return this.scheduleDelayTask(name, task, null, time);
    }

    Future scheduleDelayTask(String name, ExecutableTask task, ExecutableCallback callback, long time);

    default Future scheduleRepeatingTask(String name, ExecutableTask task, long interval) {
        return this.scheduleRepeatingTask(name, task, interval, false);
    }

    Future scheduleRepeatingTask(String name, ExecutableTask task, long interval, boolean keeplive);

    @Deprecated
    default Future scheduleRepeatingTask(ExecutableTask task, String name, long interval) {
        return this.scheduleRepeatingTask(name, task, interval);
    }

    @Deprecated
    default Future scheduleRepeatingTask(ExecutableTask task, String name, long interval, boolean keeplive) {
        return this.scheduleRepeatingTask(name, task, interval, keeplive);
    }

    List<String> getScheduledTaskList();

    List<String> getRepeatingTaskList();

    long getScheduledTasksAmount();

    long getRepeatingTasksAmount();
}
