package com.particle.executor.service;

import com.particle.executor.thread.IScheduleThread;
import com.particle.executor.thread.TimerPoolThread;

public class AsyncScheduleService extends AbstractScheduleService {

    private static final AsyncScheduleService INSTANCE = new AsyncScheduleService();

    public static AsyncScheduleService getInstance() {
        return AsyncScheduleService.INSTANCE;
    }

    //执行器缓存
    private IScheduleThread scheduleThread;

    private AsyncScheduleService() {
        //初始化Node层任务执行器
        this.scheduleThread = new TimerPoolThread("Async");
    }

    /**
     * 查询任务量最少的线程
     *
     * @return
     */
    public IScheduleThread getThread() {
        return this.scheduleThread;
    }

    public void shutdown() {
        this.scheduleThread.terminate();
    }
}
