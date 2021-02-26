package com.particle.util.timingwhell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 时间轮的主调度器
 */
public class TimerSchedule implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerSchedule.class);

    // 时间轮最小时间单位
    public static final int UNIT_INTERVAL = 16;  // 16ms
    // 最小时间单位偏移
    public static final int UNIT_OFFSET = 4; // 0b1111;

    private ScheduledExecutorService scheduleThread;
    private ExecutorService taskExecutors;

    private ITimerWheel level1;
    private ITimerWheel level2;
    private ITimerWheel level3;

    public TimerSchedule(String name) {
        this.scheduleThread = Executors.newScheduledThreadPool(1, (job) -> new Thread(job, name));
        this.taskExecutors = this.scheduleThread;

        this.level3 = new TimerWheelSchedule(6, 12);
        this.level2 = new TimerWheelSchedule(6, 6);
        this.level1 = new TimerWheelRunner(6, 0, taskExecutors);

        this.level1.setNextWheel(this.level2);
        this.level2.setSubWheel(this.level1);
        this.level2.setNextWheel(this.level3);
        this.level3.setSubWheel(this.level2);

        this.scheduleThread.scheduleAtFixedRate(this.level1::tick, 0, UNIT_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * 增加一个即时任务
     *
     * @param job
     * @param <T>
     * @return
     */
    public <T> Future<T> scheduleJob(TimerJob<T> job) {
        TimerTask<T> task = new TimerTask<>(job);

        this.level1.scheduleJob(task);

        return task;
    }

    /**
     * 增加延迟任务
     *
     * @param job
     * @param delay
     * @param <T>
     * @return
     */
    public <T> Future<T> scheduleDelayJob(TimerJob<T> job, long delay) {
        TimerTask<T> task = new TimerTask<>(job);

        // 尝试添加到一级时间轮
        if (this.level1.scheduleDelayJob(task, delay)) {
            return task;
        }

        delay = delay - this.level1.getLeftTime();
        if (this.level2.scheduleDelayJob(task, delay)) {
            return task;
        }

        delay = delay - this.level2.getLeftTime();
        if (this.level3.scheduleDelayJob(task, delay)) {
            return task;
        }

        return null;
    }

    @Override
    public void run() {
    }

    public void shutdown() {
        this.scheduleThread.shutdown();
        this.taskExecutors.shutdown();
    }
}
