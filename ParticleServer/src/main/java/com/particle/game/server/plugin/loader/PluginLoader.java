package com.particle.game.server.plugin.loader;

import com.particle.api.plugin.PluginBase;
import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.plugin.annotation.Plugin;
import com.particle.model.plugin.model.PluginDescription;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import javax.inject.Singleton;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.regex.Pattern;

@Singleton
public class PluginLoader {

    private static String JAVA_VERSION = System.getProperty("java.version");

    private static String JAVA_8_VERSION = "1.8.0";

    public static URLClassLoader customUrlLoader = null;

    /**
     * 加载jar
     *
     * @param jarFile
     */
    public PluginDescription loadPlugin(File jarFile) throws Exception {
        URL targetUrl = jarFile.toURI().toURL();
        URLClassLoader loader;
        if (compareVersion(JAVA_VERSION, JAVA_8_VERSION) >= 0) {
            if (customUrlLoader == null) {
                loader = new URLClassLoader(new URL[]{targetUrl},
                        this.getClass().getClassLoader());
                customUrlLoader = loader;
            } else {
                loader = customUrlLoader;
            }
        } else {
            loader = (URLClassLoader) this.getClass().getClassLoader();
        }

        boolean isLoader = false;
        for (URL url : loader.getURLs()) {
            if (url.equals(targetUrl)) {
                isLoader = true;
                break;
            }
        }
        // 如果没有加载
        if (!isLoader) {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            add.setAccessible(true);
            add.invoke(loader, targetUrl);
        }

        ClassLoader[] loaders = new ClassLoader[]{loader};
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().setUrls(jarFile.toURI().toURL());
        configurationBuilder.setClassLoaders(loaders);
        Reflections reflections = new Reflections(configurationBuilder);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(Plugin.class);
        if (classesList.size() > 1 || classesList.isEmpty()) {
            throw new ProphetException(ErrorCode.PLUGIG_ERROR,
                    String.format("该插件[%s]必须仅实现一个继承PluginBase的实例", jarFile.getName()));
        }
        for (Class javaClass : classesList) {
            return this.parsePluginDescription(jarFile, javaClass, Plugin.class);
        }
        return null;
    }

    /**
     * 只允许加载jar的文件
     *
     * @return
     */
    public Pattern[] getPluginFilters() {
        return new Pattern[]{Pattern.compile("^.+\\.jar$")};
    }

    /**
     * 解析PluginDescription
     *
     * @param jarFile
     * @param javaClass
     * @param annoClass
     * @return
     * @throws Exception
     */
    private PluginDescription parsePluginDescription(File jarFile,
                                                     Class javaClass,
                                                     Class<? extends Annotation> annoClass) throws Exception {
        Class<? extends PluginBase> pluginClass = javaClass.asSubclass(PluginBase.class);
        Annotation annotation = pluginClass.getAnnotation(annoClass);
        if (annotation == null) {
            throw new ProphetException(ErrorCode.CORE_EROOR, "parsePluginDescription失败!");
        }
        PluginDescription pluginDescription = new PluginDescription();
        Method valueMethod = annoClass.getMethod("id");
        valueMethod.setAccessible(true);
        pluginDescription.setId((String) valueMethod.invoke(annotation));

        valueMethod = annoClass.getMethod("name");
        valueMethod.setAccessible(true);
        pluginDescription.setName((String) valueMethod.invoke(annotation));

        valueMethod = annoClass.getMethod("version");
        valueMethod.setAccessible(true);
        pluginDescription.setVersion((String) valueMethod.invoke(annotation));

        valueMethod = annoClass.getMethod("description");
        valueMethod.setAccessible(true);
        pluginDescription.setDescription((String) valueMethod.invoke(annotation));

        pluginDescription.setPluginClass(javaClass);

        File dataFolder = new File(jarFile.getParentFile(), pluginDescription.getId());
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        } else if (dataFolder.exists() && !dataFolder.isDirectory()) {
            throw new ProphetException(ErrorCode.CONFIG_ERROR,
                    String.format("插件数据地址[%s]存在且为非目录", dataFolder.getAbsolutePath()));
        }
        pluginDescription.setJarFile(jarFile);
        pluginDescription.setDataFolder(dataFolder);
        return pluginDescription;
    }


    /**
     * 比较版本大小
     * <p>
     * 说明：支n位基础版本号+1位子版本号
     * 示例：1.0.2>1.0.1 , 1.0.1.1>1.0.1
     *
     * @param version1 版本1
     * @param version2 版本2
     * @return 0:相同 1:version1大于version2 -1:version1小于version2
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0; //版本相同
        }
        int index = -1;
        if ((index = version1.indexOf("_")) > 0) {
            version1 = version1.substring(0, index);
        }

        if ((index = version2.indexOf("_")) > 0) {
            version2 = version2.substring(0, index);
        }

        String[] v1Array = version1.split("\\.");
        String[] v2Array = version2.split("\\.");
        int v1Len = v1Array.length;
        int v2Len = v2Array.length;
        int baseLen = 0; //基础版本号位数（取长度小的）
        if (v1Len > v2Len) {
            baseLen = v2Len;
        } else {
            baseLen = v1Len;
        }

        for (int i = 0; i < baseLen; i++) { //基础版本号比较
            if (v1Array[i].equals(v2Array[i])) { //同位版本号相同
                continue; //比较下一位
            } else {
                return Integer.parseInt(v1Array[i]) > Integer.parseInt(v2Array[i]) ? 1 : -1;
            }
        }
        //基础版本相同，再比较子版本号
        if (v1Len != v2Len) {
            return v1Len > v2Len ? 1 : -1;
        } else {
            //基础版本相同，无子版本号
            return 0;
        }
    }
}
