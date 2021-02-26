package com.particle.core.ecs.component;

import com.particle.core.utils.Bits;

import java.util.List;

public abstract class AComponentContainer {

    private Bits fingerprint = new Bits();

    /**
     * 获取组件
     *
     * @param index
     * @return
     */
    abstract ECSComponent getComponent(int index);

    /**
     * 配置组件
     *
     * @param index
     * @param component
     */
    void setComponent(int index, ECSComponent component) {
        this.setComponent0(index, component);

        // 设置过滤器
        if (component == null) {
            this.fingerprint.clear(index);
        } else {
            this.fingerprint.set(index);
        }
    }

    protected abstract void setComponent0(int index, ECSComponent component);

    /**
     * 清除组件
     *
     * @param index
     */
    void resetComponent(int index) {
        this.resetComponent0(index);

        // 设置过滤器
        this.fingerprint.clear(index);
    }

    protected abstract void resetComponent0(int index);

    /**
     * 判断是否持有指定的组件
     *
     * @param index
     * @return
     */
    abstract boolean hasComponent(int index);

    /**
     * 获取该容器所含组件的指纹
     *
     * @return
     */
    public Bits getComponentsFingerprint() {
        return this.fingerprint;
    }

    public abstract List<ECSComponent> getComponents();
}