package com.particle.core.ecs.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleContainerHash extends AModuleContainer {

    /**
     * 业务缓存
     */
    private Map<Integer, ECSModule> storage = new HashMap<>();

    @Override
    public ECSModule getModule(int index) {
        return storage.get(index);
    }

    @Override
    public void setModule0(int index, ECSModule module) {
        this.storage.put(index, module);
    }

    @Override
    boolean hasModule(int index) {
        return this.storage.containsKey(index);
    }

    @Override
    public ECSModule resetModule0(int index) {
        return this.storage.remove(index);
    }

    @Override
    public List<ECSModule> getModules() {
        return new ArrayList<>(this.storage.values());
    }
}
