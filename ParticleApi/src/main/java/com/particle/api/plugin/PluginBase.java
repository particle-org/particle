package com.particle.api.plugin;

import com.particle.model.plugin.model.PluginDescription;

import javax.inject.Inject;

public abstract class PluginBase {

    /**
     * 控制加载顺序
     */
    private static int LOAD_INDEX = 0;

    private boolean isEnabled = false;

    /**
     * 插件的介绍，从注解中得到
     */
    private PluginDescription pluginDescription;


    @Inject
    public void load() {
        LOAD_INDEX++;
        this.onLoad();
    }

    /**
     * 在插件的入口类被构造的时候调用
     */
    protected abstract void onLoad();

    /**
     * 插件被enable的时候调用
     */
    protected abstract void onEnable();

    /**
     * 插件被disable的时候被调用
     */
    protected abstract void onDisable();

    /**
     * 插件的配置变更的时候调用
     *
     * @param namespace
     */
    protected abstract void onConfigurationUpdated(String namespace);

    /**
     * 设置插件是否有效
     *
     * @param enabled
     */
    public final void setEnabled(boolean enabled) {
        if (this.isEnabled != enabled) {
            isEnabled = enabled;
            if (isEnabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    /**
     * 通知配置更改
     *
     * @param namespace
     */
    public final void notifyConfigurationChange(String namespace) {
        if (this.isEnabled) {
            this.onConfigurationUpdated(namespace);
        }
    }

    /**
     * 插件是否可用
     *
     * @return
     */
    public final boolean isEnabled() {
        return isEnabled;
    }

    /**
     * 获取插件详情
     *
     * @return
     */
    public final PluginDescription getPluginDescription() {
        return pluginDescription;
    }

    /**
     * 设置插件详情
     *
     * @param pluginDescription
     */
    public final void setPluginDescription(PluginDescription pluginDescription) {
        this.pluginDescription = pluginDescription;
    }

    /**
     * load的顺序
     *
     * @return
     */
    public int loadIndex() {
        return LOAD_INDEX;
    }
}
