package com.particle.core.ecs.system;

public abstract class IntervalECSSystem implements ECSSystem {

    private int ttl;
    private int deltaTimeCount;

    public IntervalECSSystem() {
        this.reset();
    }

    @Override
    public final void tick(long deltaTime) {
        // 累计tick间隔
        this.deltaTimeCount += deltaTime;

        // 判断是否需要tick
        if (this.ttl-- < 1) {
            // 执行tick操作
            this.doTick(this.deltaTimeCount);

            // 重置状态
            this.reset();
        }
    }

    /**
     * 重置状态
     */
    private void reset() {
        this.ttl = this.getInterval();
        this.deltaTimeCount = 0;
    }

    /**
     * Tick间隔
     *
     * @return
     */
    protected abstract int getInterval();

    /**
     * 执行tick操作
     *
     * @param deltaTime
     */
    protected abstract void doTick(long deltaTime);

}
