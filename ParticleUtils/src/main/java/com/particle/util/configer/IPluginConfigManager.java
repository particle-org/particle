package com.particle.util.configer;

import com.google.inject.ProvidedBy;

import java.util.Map;

@ProvidedBy(PluginConfigManagerProvider.class)
public interface IPluginConfigManager {
    void readConfigs();

    void loadConfigBean(Object object);

    void saveAll();

    void save(String space);

    <T> T getConfig(String space, Class<T> config);

    <T> Map<String, T> getConfigs(String space, Class<T> configClass);
}
