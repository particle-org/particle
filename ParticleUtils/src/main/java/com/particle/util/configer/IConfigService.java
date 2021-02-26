package com.particle.util.configer;

import java.util.ArrayList;
import java.util.List;

public interface IConfigService {
    <T> boolean saveConfig(T t);

    <T> T loadConfig(Class<T> clazz);

    <T> boolean saveConfigs(List<T> t);

    <T> List<T> loadConfigs(Class<T> clazz);

    /**
     * 读取或使用默认配置
     *
     * @param clazz
     * @param <T>
     * @return
     */
    default <T> T loadConfigOrSaveDefault(Class<T> clazz) {
        //读取配置
        T result = this.loadConfig(clazz);

        //若读取不到则生成并保存默认配置
        if (result == null) {
            T t = null;
            try {
                t = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Config load fail!", e);
            }

            this.saveConfig(t);

            return t;
        } else {
            return result;
        }
    }

    default <T> List<T> loadConfigsOrSaveDefault(Class<T> clazz) {
        //读取配置
        List<T> result = this.loadConfigs(clazz);

        //若读取不到则生成并保存默认配置
        if (result == null) {
            List<T> configs = new ArrayList<>(1);
            try {
                configs.add(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Config load fail!", e);
            }

            this.saveConfigs(configs);

            return configs;
        } else {
            return result;
        }
    }
}
