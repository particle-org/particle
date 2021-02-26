package com.particle.core.ecs.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class ModuleContainerArray extends AModuleContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleContainerArray.class);

    /**
     * 业务缓存
     */
    private ECSModule[] storage = new ECSModule[64];

    @Override
    public ECSModule getModule(int index) {
        // 合法性检查
        if (index < 0) {
            LOGGER.warn("Illegal component index of {}!", index);
            return null;
        }

        if (index < storage.length) {
            return storage[index];
        }

        return null;
    }

    @Override
    public void setModule0(int index, ECSModule module) {
        // 合法性检查
        if (index < 0) {
            LOGGER.warn("Illegal component index of {}!", index);
            return;
        }

        // 扩展数组
        while (!(index < this.storage.length)) {
            ECSModule[] storage = new ECSModule[this.storage.length << 1];
            System.arraycopy(this.storage, 0, storage, 0, this.storage.length);

            this.storage = storage;
        }

        // 设置组件
        this.storage[index] = module;


    }

    @Override
    boolean hasModule(int index) {
        // 合法性检查
        if (index < 0) {
            LOGGER.warn("Illegal module index of {}!", index);
            return false;
        }

        if (index < storage.length) {
            return storage[index] != null;
        }

        return false;
    }

    @Override
    public ECSModule resetModule0(int index) {
        // 合法性检查
        if (index < 0 || index >= this.storage.length) {
            return null;
        }

        ECSModule cache = this.storage[index];

        // 设置组件
        this.storage[index] = null;

        return cache;
    }

    @Override
    public List<ECSModule> getModules() {
        List<ECSModule> ecsModules = new LinkedList<>();
        for (ECSModule ecsModule : this.storage) {
            if (ecsModule != null) {
                ecsModules.add(ecsModule);
            }
        }
        return ecsModules;
    }
}
