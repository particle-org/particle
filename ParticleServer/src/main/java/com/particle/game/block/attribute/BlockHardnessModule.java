package com.particle.game.block.attribute;

import com.particle.core.ecs.module.BehaviorModule;

public class BlockHardnessModule extends BehaviorModule {

    /**
     * 爆炸阻力
     */
    private double blastResistance;

    /**
     * 硬度
     */
    private double hardness;

    /**
     * 徒手破坏时间
     */
    private double baseBreakTime;

    public void update(double baseBreakTime, double hardness, double blastResistance) {
        this.blastResistance = blastResistance;
        this.hardness = hardness;
        this.baseBreakTime = baseBreakTime;

    }

    public double getBlastResistance() {
        return blastResistance;
    }

    public double getHardness() {
        return hardness;
    }

    public double getBaseBreakTime() {
        return baseBreakTime;
    }
}
