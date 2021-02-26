package com.particle.game.block.trigger.components;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.block.types.BlockPrototype;

import java.util.HashMap;
import java.util.Map;

public class EntityEnterBlockTriggerModule extends BehaviorModule {

    private Map<BlockPrototype, IEntityEnterBlockTrigger> entityEnterBlockTrigerMap = new HashMap<>();
    private long triggerInterval = 500;
    private long lastTriggerTimestamp = 0;

    /**
     * 注册触发器
     *
     * @param blockPrototype
     * @param triger
     */
    public void addTriger(BlockPrototype blockPrototype, IEntityEnterBlockTrigger triger) {
        this.entityEnterBlockTrigerMap.put(blockPrototype, triger);
    }

    /**
     * 获取触发器
     *
     * @param blockPrototype
     * @return
     */
    public IEntityEnterBlockTrigger getTriger(BlockPrototype blockPrototype) {
        return this.entityEnterBlockTrigerMap.get(blockPrototype);
    }

    public long getTriggerInterval() {
        return triggerInterval;
    }

    public void setTriggerInterval(long triggerInterval) {
        this.triggerInterval = triggerInterval;
    }

    public long getLastTriggerTimestamp() {
        return lastTriggerTimestamp;
    }

    public void setLastTriggerTimestamp(long lastTriggerTimestamp) {
        this.lastTriggerTimestamp = lastTriggerTimestamp;
    }
}
