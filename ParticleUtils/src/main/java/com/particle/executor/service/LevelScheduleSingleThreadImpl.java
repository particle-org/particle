package com.particle.executor.service;

import com.particle.executor.thread.IScheduleThread;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LevelScheduleSingleThreadImpl extends LevelScheduleService {

    /**
     * 单例对象
     */
    private static final LevelScheduleSingleThreadImpl INSTANCE = new LevelScheduleSingleThreadImpl();

    /**
     * 获取单例
     */
    public static LevelScheduleSingleThreadImpl getInstance() {
        return LevelScheduleSingleThreadImpl.INSTANCE;
    }

    private IScheduleThread levelThread;

    //执行器缓存
    private Map<String, IScheduleThread> threadMap;

    private LevelScheduleSingleThreadImpl() {
        this.levelThread = this.createThread("LevelThread");

        this.threadMap = new HashMap<>();
        this.threadMap.put("LevelThread", this.levelThread);
        this.threadMap = Collections.unmodifiableMap(this.threadMap);
    }

    @Override
    public IScheduleThread registerThread(String name) {
        return this.levelThread;
    }

    @Override
    public void shutdown(IScheduleThread thread) {
    }

    @Override
    public void shutdownAll() {
        this.destroyThread(this.levelThread);
    }

    @Override
    @Deprecated
    public IScheduleThread getThread(String threadName) {
        return this.threadMap.get(threadName);
    }

    @Override
    public IScheduleThread getDefaultThread() {
        return this.levelThread;
    }

    @Override
    public Map<String, IScheduleThread> getThreadMap() {
        return threadMap;
    }
}
