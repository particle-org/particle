package com.particle.core.ecs.system;

import java.util.*;

public class SystemContainerList extends ASystemContainer {

    /**
     * 系统缓存
     */
    private List<ECSSystem> storage = new LinkedList<>();
    private Map<Class, ECSSystem> systemMap = new HashMap<>();

    @Override
    public <T extends ECSSystem> T getECSSystem(Class<T> systemClass) {
        return (T) systemMap.get(systemClass);
    }

    @Override
    void updateSystemList(List<ECSSystem> systems) {
        this.storage = Collections.unmodifiableList(systems);
        for (ECSSystem system : systems) {
            systemMap.put(system.getClass(), system);
        }
    }

    @Override
    public List<ECSSystem> getSystemList() {
        return this.storage;
    }
}
