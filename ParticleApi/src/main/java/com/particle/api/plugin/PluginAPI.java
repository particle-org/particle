package com.particle.api.plugin;

import com.particle.model.plugin.model.PluginDescription;

import java.util.Collection;
import java.util.List;

public interface PluginAPI {

    /**
     * 根据id获取插件对象
     *
     * @param id id
     * @return 返回结果
     */
    PluginBase getPluginById(String id);

    /**
     * 获取所有的pluginBase
     *
     * @return 返回结果
     */
    Collection<PluginBase> getAllPluginBase();

    /**
     * 获取所有的PluginDescription
     *
     * @return 返回结果
     */
    List<PluginDescription> getAllPluginDescription();
}
