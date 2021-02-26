package com.particle.executor.service;

import com.particle.executor.thread.IScheduleThread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeScheduleService extends AbstractScheduleService {

    private static final NodeScheduleService INSTANCE = new NodeScheduleService();

    private static final int THREAD_AMOUNT = 4;

    //执行器缓存
    private IScheduleThread[] threadIndex;
    private Map<String, IScheduleThread> threadMap;

    public static NodeScheduleService getInstance() {
        return NodeScheduleService.INSTANCE;
    }

    private NodeScheduleService() {
        //初始化Node层任务执行器
        this.threadIndex = new IScheduleThread[THREAD_AMOUNT];
        this.threadMap = new ConcurrentHashMap<>();
        for (int i = 0; i < THREAD_AMOUNT; i++) {
            //配置名称
            String threadID = "Node_" + i;

            //创建线程
            IScheduleThread thread = this.createThread(threadID);

            //缓存Index
            this.threadIndex[i] = thread;

            //缓存Map
            this.threadMap.put(threadID, thread);
        }
    }

    public IScheduleThread getThread(int identifiedId) {
        return this.threadIndex[identifiedId & 3];
    }

    @Deprecated
    public IScheduleThread getThread(String threadName) {
        return this.threadMap.get(threadName);
    }

    public void shutdown() {
        for (IScheduleThread thread : this.threadIndex) {
            if (thread != null) {
                this.destroyThread(thread);
            }
        }
    }

    public Map<String, IScheduleThread> getThreadMap() {
        return threadMap;
    }
}
