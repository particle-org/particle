package com.particle.game.server.plugin;

import com.google.inject.Injector;
import com.particle.api.plugin.PluginAPI;
import com.particle.api.plugin.PluginBase;
import com.particle.game.server.plugin.loader.PluginLoader;
import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.plugin.model.PluginDescription;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URLClassLoader;
import java.util.*;
import java.util.regex.Pattern;

public class PluginManager implements PluginAPI {

    private static final Logger logger = LoggerFactory.getLogger(PluginManager.class);

    private static PluginLoader pluginLoader = new PluginLoader();

    /**
     * 存储所有的插件索引
     */
    private static Map<String, PluginBase> allPlugins = new HashMap<>();

    /**
     * 存储所有的PluginDescription
     */
    private static List<PluginDescription> allPluginDescriptions = new ArrayList<>();

    // 构造对象
    private static List<PluginBase> sortedPluginBase = new ArrayList<>();

    private static volatile PluginManager instance = null;

    /**
     * 单例
     *
     * @return
     */
    public static PluginManager getInstance() {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager();
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造函数
     */
    private PluginManager() {

    }


    @Override
    public PluginBase getPluginById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return this.allPlugins.get(id);
    }

    @Override
    public Collection<PluginBase> getAllPluginBase() {
        return this.allPlugins.values();
    }

    @Override
    public List<PluginDescription> getAllPluginDescription() {
        return this.allPluginDescriptions;
    }

    /**
     * 获取自定义urlClassLoader
     *
     * @return
     */
    public URLClassLoader getCustomUrlClassLoader() {
        return PluginLoader.customUrlLoader;
    }

    /**
     * 加载目录下的所有合法插件
     *
     * @param dictionary
     */
    public boolean loadPlugins(String dictionary) {
        return this.loadPlugins(new File(dictionary));
    }

    /**
     * 加载目录下的所有插件
     * 只加载第一级目录的jar
     *
     * @param dictionary
     */
    private boolean loadPlugins(File dictionary) {
        if (!dictionary.exists()) {
            dictionary.mkdirs();
        }
        if (!dictionary.isDirectory()) {
            logger.error("目标插件加载地址【{}】不是目录", dictionary.getAbsolutePath());
            return false;
        }
        try {
            // 加载jar，配置数据目录
            Pattern[] patterns = pluginLoader.getPluginFilters();
            for (File jarFile : dictionary.listFiles(((dir, name) -> {
                for (Pattern pattern : patterns) {
                    if (pattern.matcher(name).matches()) {
                        return true;
                    }
                }
                return false;
            }))) {
                if (jarFile.isDirectory()) {
                    continue;
                }
                PluginDescription pluginDescription = this.pluginLoader.loadPlugin(jarFile);
                if (pluginDescription == null) {
                    throw new ProphetException(ErrorCode.PLUGIG_ERROR,
                            String.format("该插件[%s]无法读取有效的Plugin注解入口", jarFile.getName()));
                }
                this.checkValid(pluginDescription);
                this.allPluginDescriptions.add(pluginDescription);
            }

            return true;
        } catch (Exception e) {
            logger.error("加载插件失败！", e);
            return false;
        }
    }

    /**
     * 检测插件是否合法
     *
     * @param pluginDescription
     */
    private void checkValid(PluginDescription pluginDescription) {
        for (PluginDescription description : this.allPluginDescriptions) {
            if (description.getId().equalsIgnoreCase(pluginDescription.getId())) {
                throw new ProphetException(ErrorCode.PLUGIG_ERROR, String.format("存在相同的插件[%s]", pluginDescription.getId()));
            }
        }
    }

    /**
     * 构造各个插件的基类
     *
     * @param injector
     * @return
     */
    public boolean instance(Injector injector) {
        try {
            for (PluginDescription pluginDescription : this.allPluginDescriptions) {
                if (this.allPlugins.containsKey(pluginDescription.getId())) {
                    throw new ProphetException(ErrorCode.PLUGIG_ERROR, String.format("具有重复插件[%s]", pluginDescription.getId()));
                }
                Class classes = pluginDescription.getPluginClass();

                Class<? extends PluginBase> pluginClass = classes.asSubclass(PluginBase.class);
                Annotation annotation = pluginClass.getAnnotation(javax.inject.Singleton.class);
                if (annotation == null) {
                    annotation = pluginClass.getAnnotation(com.google.inject.Singleton.class);
                }
                if (annotation == null) {
                    throw new ProphetException(ErrorCode.PLUGIG_ERROR, String.format("class[%s]必须增加[@Singleton]注解", classes));
                }
                PluginBase pluginBase = injector.getInstance(pluginClass);
                if (pluginBase == null) {
                    pluginBase = pluginClass.newInstance();
                    injector.injectMembers(pluginBase);
                }
                pluginBase.setPluginDescription(pluginDescription);
                sortedPluginBase.add(pluginBase);
                this.allPlugins.put(pluginDescription.getId(), pluginBase);
            }

            // 按顺序enable调用
            sortedPluginBase.sort((o1, o2) -> {
                if (o1.loadIndex() < o2.loadIndex()) {
                    return -1;
                } else if (o1.loadIndex() > o2.loadIndex()) {
                    return 1;
                } else {
                    return 0;
                }
            });
            return true;
        } catch (Exception e) {
            logger.error("instance插件失败！", e);
            return false;
        }
    }

    /**
     * enable插件
     *
     * @return
     */
    public boolean enable() {
        try {
            for (PluginBase pluginBase : sortedPluginBase) {
                pluginBase.setEnabled(true);
            }
            return true;
        } catch (Exception | Error e) {
            logger.error("enable插件失败！", e);
            return false;
        }
    }

    /**
     * disable插件
     *
     * @return
     */
    public boolean disable() {
        try {
            for (PluginBase pluginBase : sortedPluginBase) {
                pluginBase.setEnabled(false);
            }
            return true;
        } catch (Exception e) {
            logger.error("disable插件失败！", e);
            return false;
        }
    }
}
