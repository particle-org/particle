package com.particle.executor.service;

import com.particle.executor.thread.IScheduleThread;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LevelScheduleThreadPoolImpl extends LevelScheduleService {

    /**
     * 单例对象
     */
    private static final LevelScheduleThreadPoolImpl INSTANCE = new LevelScheduleThreadPoolImpl();

    /**
     * 获取单例
     */
    public static LevelScheduleThreadPoolImpl getInstance() {
        return LevelScheduleThreadPoolImpl.INSTANCE;
    }

    private static final int THREAD_AMOUNT = 2;

    private IScheduleThread defaultThread;

    //执行器缓存
    private IScheduleThread[] threadIndex;
    private Map<String, IScheduleThread> threadMap;

    private LevelScheduleThreadPoolImpl() {
        //初始化Level层任务执行器
        this.threadIndex = new IScheduleThread[THREAD_AMOUNT];
        this.threadMap = new HashMap<>();
        for (int i = 0; i < THREAD_AMOUNT; i++) {
            //配置名称
            String threadID = "Level_" + i;

            //创建线程
            IScheduleThread thread = this.createThread(threadID);

            //缓存Index
            this.threadIndex[i] = thread;

            //缓存Map
            this.threadMap.put(threadID, thread);
        }

        // 禁止修改
        this.threadMap = Collections.unmodifiableMap(this.threadMap);

        //创建默认线程
        this.defaultThread = this.threadIndex[0];
    }

    @Override
    public IScheduleThread registerThread(String name) {
        //配置名称
        String threadID = "Level_" + name;

        //创建线程
        return this.threadIndex[threadID.hashCode() % THREAD_AMOUNT];
    }

    @Override
    public void shutdown(IScheduleThread thread) {
    }

    @Override
    public void shutdownAll() {
        for (IScheduleThread scheduleThread : this.threadIndex) {
            this.destroyThread(scheduleThread);
        }
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
