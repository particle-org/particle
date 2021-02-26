package com.particle.util.timingwhell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractTimerWheel implements ITimerWheel {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTimerWheel.class);

    // 时间轮尺寸
    protected final int whellSize;
    // 时间轮快速计算掩码
    protected final int whellMask;
    // 时间偏移量
    protected final int timeOffset;

    // 时间轮tick单位列表
    protected final TimerUnit[] timeUnits;

    // 当前调度位置的节点（当前执行的节点下一个节点）
    protected int scheduledIndex;

    public AbstractTimerWheel(int maskLength, int maskOffset) {
        // 初始化配置参数
        this.whellSize = 1 << maskLength;
        this.whellMask = this.whellSize - 1;
        this.timeOffset = maskOffset + TimerSchedule.UNIT_OFFSET;

        // 创建节点
        this.timeUnits = new TimerUnit[this.whellSize];
        for (int i = 0; i < this.timeUnits.length; i++) {
            this.timeUnits[i] = new TimerUnit();
        }

        // 初始化索引
        this.scheduledIndex = 1;
    }

    /**
     * tick节点
     * 1. 跳转至下一个节点
     * 2. 检查是否循环一周
     * 3. 执行当前节点中的任务
     * <p>
     * 这种设计执行业务实际是执行current的next节点，这样有利于降低即时任务的运行延迟
     */
    @Override
    public void tick() {
        // Step 1 : 移动下标，保证新任务被正确调度
        this.scheduledIndex = (this.scheduledIndex + 1) & this.whellMask;

        // Step 2 ： 获取tick节点
        TimerUnit tickNode = this.timeUnits[this.scheduledIndex];

        // Step 3 : tick节点
        this.doTick(tickNode.fetchTask());

        // Step 4 : tick轮转后的回调
        if (this.scheduledIndex == 0) {
            this.doTickNext();
        }
    }

    @Override
    public long getLeftTime() {
        return (this.whellSize - this.scheduledIndex) << this.timeOffset;
    }

    protected abstract void doTick(List<TimerTask> timerTasks);

    protected abstract void doTickNext();
}
