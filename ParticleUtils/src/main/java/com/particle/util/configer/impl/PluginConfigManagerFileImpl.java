package com.particle.util.configer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.particle.util.configer.IPluginConfigManager;
import com.particle.util.configer.anno.PluginConfigBean;
import com.particle.util.configer.exception.ConfigBeanNotDetectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
public class PluginConfigManagerFileImpl implements IPluginConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginConfigManagerFileImpl.class);

    private static final String CONFIG_DIR = "plugins";

    private Map<String, Object> configData = new HashMap<>();

    private Set<String> configBeans = new HashSet<>();

    /**
     * 读取已有的配置文件
     */
    @Override
    public void readConfigs() {
        // 读取配置文件目录
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 加载所有的配置文件
        for (File pluginConfigDir : dir.listFiles()) {
            if (pluginConfigDir.isDirectory()) {

                for (File pluginConfigFile : pluginConfigDir.listFiles()) {
                    if (!pluginConfigFile.getName().endsWith(".json")) {
                        continue;
                    }

                    if (!this.configData.containsKey(pluginConfigDir.getName())) {
                        this.configData.put(pluginConfigDir.getName(), new HashMap<String, Object>());
                    }

                    try {
                        // 读取配置文件
                        // todo 对于嵌套的配置，会生成很大的文件
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(pluginConfigFile), "utf8"));

                        StringBuilder stringBuilder = new StringBuilder();
                        String temp = "";
                        while ((temp = bufferedReader.readLine()) != null) {
                            stringBuilder.append(temp);
                        }

                        bufferedReader.close();

                        // 解析配置
                        JSONObject configObject = JSONObject.parseObject(stringBuilder.toString());

                        // 解析文件名
                        String space = pluginConfigFile.getName();


                        this.putConfig((Map<String, Object>) this.configData.get(pluginConfigDir.getName()), space.substring(0, space.lastIndexOf(".")), configObject.getInnerMap(), true);
                    } catch (Exception e) {
                        LOGGER.error("Fail to load file {}.", pluginConfigFile.getAbsolutePath());
                    }
                }
            }
        }
    }

    /**
     * 检查ConfigBean
     *
     * @param object
     */
    @Override
    public void loadConfigBean(Object object) {
        PluginConfigBean configBeanDetail = object.getClass().getDeclaredAnnotation(PluginConfigBean.class);
        if (configBeanDetail == null) {
            throw new ConfigBeanNotDetectedException();
        }

        // 读取配置名称
        String space = configBeanDetail.name();

        // 解析分界点
        int splitIndex = space.indexOf(".");
        if (splitIndex == -1) {
            throw new RuntimeException("Missing namespace");
        }

        // 读取namespace
        String namespace = space.substring(0, splitIndex);
        space = space.substring(splitIndex + 1);

        // 注册config bean
        this.configBeans.add(space);

        Map<String, Object> configData = JSON.parseObject(JSON.toJSONString(object, SerializerFeature.IgnoreNonFieldGetter)).getInnerMap();

        if (this.configData.containsKey(namespace)) {
            this.putConfig((Map<String, Object>) this.configData.get(namespace), space, configData, false);
        } else {
            Map<String, Object> subDir = new HashMap<>();

            this.configData.put(namespace, subDir);

            this.putConfig(subDir, space, configData, false);
        }
    }

    /**
     * 保存所有配置文件
     */
    @Override
    public void saveAll() {
        this.configData.forEach((namespace, data) -> {
            // 检查目录
            File dir = new File(CONFIG_DIR + "/" + namespace);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 非法数据
            if (!(data instanceof Map)) {
                return;
            }

            this.saveConfig(dir, "", (Map<String, Object>) data);
        });
    }

    /**
     * 保存指定路径配置文件
     */
    @Override
    public void save(String space) {
        // 解析分界点
        int splitIndex = space.indexOf(".");
        if (splitIndex == -1) {
            throw new RuntimeException("Missing namespace");
        }

        String namespace = space.substring(0, splitIndex);

        // 检查目录
        File dir = new File(CONFIG_DIR + "/" + namespace);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 迭代查找配置
        Map<String, Object> configData = (Map<String, Object>) this.configData.get(namespace);
        String subSpace = space.substring(splitIndex + 1);
        while (subSpace.contains(".")) {
            String key = subSpace.substring(0, subSpace.indexOf("."));

            configData = (Map<String, Object>) configData.get(key);
            if (configData == null || (!(configData instanceof Map))) {
                throw new RuntimeException("Config type miss match");
            }

            subSpace = subSpace.substring(subSpace.indexOf(".") + 1);
        }
        configData = (Map<String, Object>) configData.get(subSpace);

        // 保存配置
        this.saveConfig(dir, space.substring(splitIndex + 1), configData);
    }

    @Override
    public <T> T getConfig(String space, Class<T> configClass) {
        // 解析space
        String[] splitSpace = space.split("\\.");

        // 递归构造配置结构
        Map<String, Object> dataMap = this.configData;
        for (int i = 0; i < splitSpace.length; i++) {
            if (dataMap.containsKey(splitSpace[i])) {
                Object data = dataMap.get(splitSpace[i]);
                if (data instanceof Map) {
                    // 如果已经有节点，则检查该节点是否是HashMap
                    dataMap = (Map<String, Object>) data;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        // 解析配置
        return (new JSONObject(dataMap)).toJavaObject(configClass);
    }

    @Override
    public <T> Map<String, T> getConfigs(String space, Class<T> configClass) {
        // 解析space
        String[] splitSpace = space.split("\\.");

        // 递归构造配置结构
        Map<String, Object> dataMap = this.configData;
        for (int i = 0; i < splitSpace.length; i++) {
            if (dataMap.containsKey(splitSpace[i])) {
                Object data = dataMap.get(splitSpace[i]);
                if (data instanceof Map) {
                    // 如果已经有节点，则检查该节点是否是HashMap
                    dataMap = (Map<String, Object>) data;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        Map<String, T> configs = new HashMap<>(dataMap.size());
        dataMap.forEach((key, data) -> {
            if (data instanceof Map) {
                configs.put(key, (new JSONObject((Map<String, Object>) data)).toJavaObject(configClass));
            }
        });

        // 解析配置
        return configs;
    }

    private void putConfig(Map<String, Object> dataMap, String space, Map<String, Object> configData, boolean override) {
        // 解析space
        String[] splitSpace = space.split("\\.");

        // 递归构造配置结构
        for (int i = 0; i < splitSpace.length; i++) {
            if (dataMap.containsKey(splitSpace[i])) {
                Object data = dataMap.get(splitSpace[i]);
                if (data instanceof Map) {
                    // 如果已经有节点，则检查该节点是否是HashMap
                    dataMap = (Map<String, Object>) data;
                } else {
                    LOGGER.error("Type miss match.");

                    // 节点类型错误，则递归创建节点
                    Map<String, Object> child = new HashMap<>();
                    dataMap.put(splitSpace[i], child);
                    dataMap = child;
                }
            } else {
                // 没有节点，则递归创建节点
                Map<String, Object> child = new HashMap<>();
                dataMap.put(splitSpace[i], child);
                dataMap = child;
            }
        }

        // 放置配置项
        for (Map.Entry<String, Object> configEntry : configData.entrySet()) {
            if (override || !dataMap.containsKey(configEntry.getKey())) {
                dataMap.put(configEntry.getKey(), configEntry.getValue());
            }
        }
    }

    /**
     * 保存配置到文件
     *
     * @param dir
     * @param currentPath
     * @param configData
     */
    private void saveConfig(File dir, String currentPath, Map<String, Object> configData) {
        // 如果检测到对应的configBean，则直接序列化整份map
        if (this.configBeans.contains(currentPath)) {
            File pluginConfigFile = new File(dir, currentPath + ".json");
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pluginConfigFile), "utf8"));

                String configString = JSON.toJSONString(configData, SerializerFeature.PrettyFormat, SerializerFeature.IgnoreNonFieldGetter);

                bufferedWriter.write(configString);

                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                LOGGER.error("Fail to save file {}.", pluginConfigFile.getAbsolutePath());
            }
        } else {
            // 依次迭代所有成员
            configData.forEach((key, data) -> {
                if (data instanceof Map) {
                    // 构建子path
                    String subpath = currentPath;
                    if (subpath.length() != 0) {
                        subpath = subpath + ".";
                    }
                    subpath = subpath + key;

                    // 迭代保存
                    this.saveConfig(dir, subpath, (Map<String, Object>) data);
                }
            });
        }


    }
}
