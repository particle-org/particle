package com.particle.executor.model;

import com.particle.executor.thread.IScheduleThread;

import java.util.concurrent.Executor;

public class ScheduledExecutor implements Executor {

    private String name;
    private IScheduleThread scheduleThread;

    public ScheduledExecutor(String name, IScheduleThread scheduleThread) {
        this.scheduleThread = scheduleThread;
        this.name = name;
    }

    @Override
    public void execute(Runnable command) {
        this.scheduleThread.scheduleSimpleTask(name, command::run);
    }
}
