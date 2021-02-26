package com.particle.util.timingwhell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 时间轮
 */
public class TimerWheelRunner extends AbstractTimerWheel {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerWheelRunner.class);

    // 执行线程
    private ExecutorService executorService;
    private ITimerWheel nextWheel;

    public TimerWheelRunner(int maskLength, int maskOffset, ExecutorService executorService) {
        super(maskLength, maskOffset);

        // 配置执行线程
        this.executorService = executorService;
    }

    /**
     * 增加一个即时任务
     * 直接在当前调度位置增加一个任务
     *
     * @param task
     * @return
     */
    @Override
    public boolean scheduleJob(TimerTask task) {
        this.executorService.submit(task);

        return true;
    }

    /**
     * 增加延迟任务
     *
     * @param task
     * @param delay
     * @return
     */
    @Override
    public boolean scheduleDelayJob(TimerTask task, long delay) {
        // 计算下标
        long index = delay >> this.timeOffset;

        // 检查是否需要立刻执行
        if (index == 0) {
            this.executorService.submit(task);
            return true;
        }

        // 检查是否发生时间轮溢出
        if (index > this.whellMask) {
            return false;
        }

        // 添加任务
        this.timeUnits[(int) ((this.scheduledIndex + index) & whellMask)].addTask(task);
        return true;
    }

    public void setNextWheel(ITimerWheel nextWheel) {
        this.nextWheel = nextWheel;
    }

    @Override
    public void setSubWheel(ITimerWheel subWheel) {

    }

    @Override
    protected void doTick(List<TimerTask> timerTasks) {
        // 执行节点中的任务
        for (TimerTask timerTask : timerTasks) {
            this.executorService.submit(timerTask);
        }
    }

    @Override
    protected void doTickNext() {
        this.nextWheel.tick();
    }
}
