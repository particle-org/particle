package com.particle.model.entity.component.farming;

import com.particle.core.ecs.module.BehaviorModule;

public class PlantGrowUpProgressModule extends BehaviorModule {

    // TODO: 2019/12/5 该模块在升级ECS时发现其序列化方法并没有实现，这会导致所有的植物都不生长，需要补充实现

    private long futureUpdateTime;

    private int growSpeed = 50;

    private int groupValue = 0;

    public PlantGrowUpProgressModule(int growSpeed) {
        this.growSpeed = growSpeed;
        futureUpdateTime = System.currentTimeMillis();
        this.randomFutureUpdateTime();
    }

    public PlantGrowUpProgressModule() {
        futureUpdateTime = System.currentTimeMillis();
        this.randomFutureUpdateTime();
    }

    public int getGroupValue() {
        return groupValue;
    }

    public void setGroupValue(int groupValue) {
        this.groupValue = groupValue;
    }

    public long getFutureUpdateTime() {
        return futureUpdateTime;
    }

    public void setFutureUpdateTime(long futureUpdateTime) {
        this.futureUpdateTime = futureUpdateTime;
    }

    /**
     * 随机下一次更新时间
     */
    public void randomFutureUpdateTime() {
        this.futureUpdateTime = this.futureUpdateTime + (50 + (long) (Math.random() * (growSpeed))) * 1000;
    }
}
