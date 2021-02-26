package com.particle.executor.service;

import com.particle.executor.thread.IScheduleThread;

import java.util.Map;

public abstract class LevelScheduleService extends AbstractScheduleService {

    private static final LevelScheduleService INSTANCE = LevelScheduleSingleThreadImpl.getInstance();

    public static LevelScheduleService getInstance() {
        return LevelScheduleService.INSTANCE;
    }

    public abstract IScheduleThread registerThread(String name);

    public abstract void shutdown(IScheduleThread thread);

    public abstract void shutdownAll();

    @Deprecated
    public abstract IScheduleThread getThread(String threadName);

    public abstract IScheduleThread getDefaultThread();

    public abstract Map<String, IScheduleThread> getThreadMap();
}
