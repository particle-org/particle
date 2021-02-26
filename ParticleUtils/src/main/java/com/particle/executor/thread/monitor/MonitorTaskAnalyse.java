package com.particle.executor.thread.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonitorTaskAnalyse {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorTaskAnalyse.class);

    // 记录执行时间过长的任务的阈值 50ms
    private static final long TASK_LOG_RUN_TIME_THREASHOLD = 50000000;
    // 记录等待时间过长的任务的阈值 2s
    private static final long TASK_LOG_WAIT_TIME_THREASHOLD = 2 * 1000 * 1000000;

    /**
     * 单例对象
     */
    private static final MonitorTaskAnalyse INSTANCE = new MonitorTaskAnalyse();

    /**
     * 获取单例
     */
    public static MonitorTaskAnalyse getInstance() {
        return MonitorTaskAnalyse.INSTANCE;
    }

    private Map<String, MonitorTaskCounter> recorder = new ConcurrentHashMap<>();
    private Map<String, Long> threadWorkingTime = new HashMap<>();

    public void analyse(String threadId, MonitorTask monitorTask) {
        // 记录任务运行数据
        MonitorTaskCounter monitorTaskCounter = this.recorder.get(monitorTask.getName());
        if (monitorTaskCounter == null) {
            monitorTaskCounter = new MonitorTaskCounter(monitorTask.getName());
            this.recorder.put(monitorTaskCounter.getName(), monitorTaskCounter);
        }
        monitorTaskCounter.recorder(monitorTask);

        // 记录线程繁忙程度
        Long useTime = this.threadWorkingTime.get(threadId);
        if (useTime == null) {
            useTime = 0L;
        }
        useTime += monitorTask.getRunningTime();
        this.threadWorkingTime.put(threadId, useTime);

        // 记录高耗时/高等待任务
        if (monitorTask.getRunningTime() > TASK_LOG_RUN_TIME_THREASHOLD) {
            LOGGER.warn("Task {} running in thread {} tooooo long! cost {}ms", monitorTask.getName(), threadId, monitorTask.getRunningTime() / 1000000);
        }
        if (monitorTask.getWaitingTime() > TASK_LOG_WAIT_TIME_THREASHOLD) {
            LOGGER.warn("Task {} waiting in thread {} tooooo long! cost {}ms", monitorTask.getName(), threadId, monitorTask.getWaitingTime() / 1000000);
        }
    }

    public Map<String, MonitorTaskCounter> getRecorder() {
        return recorder;
    }

    public Map<String, Long> getThreadWorkingTime() {
        return threadWorkingTime;
    }
}
