package com.particle.core.ecs.system;

import java.util.List;

public abstract class ASystemContainer {

    /**
     * 查询ECS系统
     *
     * @param systemClass
     * @return
     */
    abstract <T extends ECSSystem> T getECSSystem(Class<T> systemClass);

    /**
     * 获取组件
     *
     * @param systems
     * @return
     */
    abstract void updateSystemList(List<ECSSystem> systems);

    /**
     * 获取系统列表
     *
     * @return
     */
    public abstract List<ECSSystem> getSystemList();

}