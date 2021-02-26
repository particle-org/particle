package com.particle.core.ecs.system;

public interface ECSSystem {

    /**
     * tick方法，传入相差的时间
     *
     * @param deltaTime
     */
    void tick(long deltaTime);

}
