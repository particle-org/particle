package com.particle.model.plugin.model;

import java.io.File;

public class PluginDescription {

    /**
     * 插件的id，必须唯一
     */
    private String id;

    /**
     * 插件的显示名称
     */
    private String name;

    /**
     * 插件的版本
     */
    private String version;

    /**
     * 插件介绍
     */
    private String description;

    /**
     * 数据目录
     */
    private File dataFolder;

    /**
     * 入口类
     */
    private Class pluginClass;

    /**
     * jar文件地址
     */
    private File jarFile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public void setDataFolder(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public Class getPluginClass() {
        return pluginClass;
    }

    public void setPluginClass(Class pluginClass) {
        this.pluginClass = pluginClass;
    }

    public File getJarFile() {
        return jarFile;
    }

    public void setJarFile(File jarFile) {
        this.jarFile = jarFile;
    }
}
