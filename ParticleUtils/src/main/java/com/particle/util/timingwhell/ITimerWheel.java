package com.particle.util.timingwhell;

public interface ITimerWheel {
    /**
     * 提交一个即时任务
     *
     * @param task
     * @return
     */
    boolean scheduleJob(TimerTask task);

    /**
     * 提交一个延迟任务
     *
     * @param task
     * @param delay
     * @return
     */
    boolean scheduleDelayJob(TimerTask task, long delay);

    /**
     * tick时间轮
     */
    void tick();

    long getLeftTime();

    void setSubWheel(ITimerWheel subWheel);

    void setNextWheel(ITimerWheel nextWheel);
}
