package com.particle.executor.service;

import com.particle.executor.model.ScheduledExecutor;
import com.particle.executor.thread.IScheduleThread;

public class ExecutorFactory {

    public static ScheduledExecutor buildExecutor(String name, IScheduleThread scheduleThread) {
        return new ScheduledExecutor(name, scheduleThread);
    }

}
