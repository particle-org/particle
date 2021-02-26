package com.particle.core.ecs;

import com.particle.core.ecs.component.AComponentContainer;
import com.particle.core.ecs.component.ComponentContainerHash;
import com.particle.core.ecs.module.AModuleContainer;
import com.particle.core.ecs.module.ModuleContainerHash;
import com.particle.core.ecs.system.ASystemContainer;
import com.particle.core.ecs.system.SystemContainerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class GameObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameObject.class);

    // 自增ID生成器
    private static final AtomicLong ID_GENERATE = new AtomicLong(1);

    // 自增id
    public final long ID = ID_GENERATE.getAndIncrement();

    // 上次Tick时间
    private long lastTickTimestamp = System.currentTimeMillis();

    private AComponentContainer componentContainer = new ComponentContainerHash();

    private AModuleContainer moduleContainer = new ModuleContainerHash();

    private ASystemContainer systemContainer = new SystemContainerList();

    public long getTickInterval() {
        return System.currentTimeMillis() - lastTickTimestamp;
    }

    public void updateLastTickTimestamp() {
        this.lastTickTimestamp = System.currentTimeMillis();
    }

    /**
     * 获取系统列表
     */
    public ASystemContainer getSystemContainer() {
        return this.systemContainer;
    }

    public AModuleContainer getModuleContainer() {
        return this.moduleContainer;
    }

    public AComponentContainer getComponentContainer() {
        return componentContainer;
    }
}
