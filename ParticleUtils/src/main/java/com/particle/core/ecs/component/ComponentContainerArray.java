package com.particle.core.ecs.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class ComponentContainerArray extends AComponentContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentContainerArray.class);

    /**
     * 组件缓存
     */
    private ECSComponent[] storage = new ECSComponent[64];

    @Override
    public ECSComponent getComponent(int index) {
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
    public void setComponent0(int index, ECSComponent component) {
        // 合法性检查
        if (index < 0) {
            LOGGER.warn("Illegal component index of {}!", index);
            return;
        }

        // 扩展数组
        while (!(index < this.storage.length)) {
            ECSComponent[] storage = new ECSComponent[this.storage.length << 1];
            System.arraycopy(this.storage, 0, storage, 0, this.storage.length);

            this.storage = storage;
        }

        // 设置组件
        this.storage[index] = component;
    }

    @Override
    public void resetComponent0(int index) {
        // 合法性检查
        if (index < 0 || index >= this.storage.length) {
            return;
        }

        // 设置组件
        this.storage[index] = null;
    }

    @Override
    public boolean hasComponent(int index) {
        // 合法性检查
        if (index < 0) {
            LOGGER.warn("Illegal component index of {}!", index);
            return false;
        }

        if (index < storage.length) {
            return storage[index] != null;
        }

        return false;
    }

    @Override
    public List<ECSComponent> getComponents() {
        List<ECSComponent> ecsComponents = new LinkedList<>();
        for (ECSComponent ecsComponent : this.storage) {
            if (ecsComponent != null) {
                ecsComponents.add(ecsComponent);
            }
        }
        return ecsComponents;
    }
}
