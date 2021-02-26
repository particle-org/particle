package com.particle.core.ecs.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentContainerHash extends AComponentContainer {

    /**
     * 组件缓存
     */
    private Map<Integer, ECSComponent> storage = new HashMap<>();

    @Override
    public ECSComponent getComponent(int index) {
        return this.storage.get(index);
    }

    @Override
    public void setComponent0(int index, ECSComponent component) {
        this.storage.put(index, component);
    }

    @Override
    public void resetComponent0(int index) {
        this.storage.remove(index);
    }

    @Override
    public boolean hasComponent(int index) {
        return this.storage.containsKey(index);
    }

    @Override
    public List<ECSComponent> getComponents() {
        return new ArrayList<>(this.storage.values());
    }
}
