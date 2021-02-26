package com.particle.core.ecs.module;

import com.particle.core.utils.Bits;

import java.util.List;

public abstract class AModuleContainer {

    private Bits fingerprint = new Bits();

    /**
     * 获取业务
     *
     * @param index
     * @return
     */
    abstract ECSModule getModule(int index);

    /**
     * 配置业务
     *
     * @param index
     * @param module
     */
    void setModule(int index, ECSModule module) {
        this.setModule0(index, module);

        // 设置过滤器
        if (module == null) {
            this.fingerprint.clear(index);
        } else {
            this.fingerprint.set(index);
        }
    }

    protected abstract void setModule0(int index, ECSModule module);

    /**
     * 清除业务
     *
     * @param index
     */
    ECSModule resetModule(int index) {
        // 设置过滤器
        this.fingerprint.clear(index);

        return this.resetModule0(index);
    }

    /**
     * 判断是否有指定业务
     *
     * @param index
     * @return
     */
    abstract boolean hasModule(int index);

    protected abstract ECSModule resetModule0(int index);

    /**
     * 获取业务列表
     *
     * @return
     */
    public Bits getServiceFingerprint() {
        return fingerprint;
    }

    public abstract List<ECSModule> getModules();
}
