package com.particle.executor.service;

import com.particle.executor.thread.IScheduleThread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LevelScheduleThreadBindImpl extends LevelScheduleService {

    /**
     * 单例对象
     */
    private static final LevelScheduleThreadBindImpl INSTANCE = new LevelScheduleThreadBindImpl();

    /**
     * 获取单例
     */
    public static LevelScheduleThreadBindImpl getInstance() {
        return LevelScheduleThreadBindImpl.INSTANCE;
    }

    //执行器缓存
    private Map<String, IScheduleThread> threadMap;

    private IScheduleThread defaultThread;

    private LevelScheduleThreadBindImpl() {
        //初始化Level层任务执行器
        this.threadMap = new ConcurrentHashMap<>();

        //创建默认线程
        this.defaultThread = this.createThread("LevelSchedule_default");
        this.threadMap.put("LevelSchedule_default", this.defaultThread);
    }

    @Override
    public IScheduleThread registerThread(String name) {
        //配置名称
        String threadID = "Level_" + name;

        //创建线程
        IScheduleThread thread = this.createThread(threadID);

        //缓存Map
        this.threadMap.put(threadID, thread);

        return thread;
    }

    @Override
    public void shutdown(IScheduleThread thread) {
        //缓存Map
        this.threadMap.remove(thread.getName());

        this.destroyThread(thread);
    }

    @Override
    public void shutdownAll() {
        this.threadMap.forEach((name, thread) -> this.destroyThread(thread));
        this.threadMap.clear();

        this.destroyThread(this.defaultThread);
    }

    @Override
    @Deprecated
    public IScheduleThread getThread(String threadName) {
        return this.threadMap.get(threadName);
    }

    @Override
    public IScheduleThread getDefaultThread() {
        return defaultThread;
    }

    @Override
    public Map<String, IScheduleThread> getThreadMap() {
        return threadMap;
    }
}
