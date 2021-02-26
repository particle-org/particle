package com.particle.util.timingwhell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 时间轮
 */
public class TimerWheelSchedule extends AbstractTimerWheel {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerWheelSchedule.class);

    // 下一级时间轮
    private ITimerWheel subWheel;
    private ITimerWheel nextWheel;
    private long subWheelMask;

    public TimerWheelSchedule(int maskLength, int maskOffset) {
        super(maskLength, maskOffset);

        this.subWheelMask = (1 << (maskOffset + TimerSchedule.UNIT_OFFSET)) - 1;
    }

    public void setSubWheel(ITimerWheel subWheel) {
        this.subWheel = subWheel;
    }

    public void setNextWheel(ITimerWheel nextWheel) {
        this.nextWheel = nextWheel;
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
        return false;
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
        long index = (delay >> this.timeOffset) + 1;

        // 检查是否发生时间轮溢出
        if (index > this.whellMask) {
            return false;
        }

        // 添加任务
        task.setDelay(delay & this.subWheelMask);
        this.timeUnits[(int) ((this.scheduledIndex + index) & whellMask)].addTask(task);
        return true;
    }

    @Override
    protected void doTick(List<TimerTask> timerTasks) {
        // 执行节点中的任务
        for (TimerTask timerTask : timerTasks) {
            boolean state = this.subWheel.scheduleDelayJob(timerTask, timerTask.getDelay());
            if (!state) {
                LOGGER.error("Fail to schedule task.");
            }
        }
    }

    @Override
    protected void doTickNext() {
        if (this.nextWheel != null) {
            this.nextWheel.tick();
        }
    }
}
