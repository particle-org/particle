package com.particle.core.ecs.serialization;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializationTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializationTool.class);

    /**
     * 序列化工具库
     * <p>
     * 将序列化方式与组件分离，有利于序列化方式的迭代改进，比如升级成Document结构，不会给组件带来大量复杂依赖
     * <p>
     * key: 存档id
     * val：序列化类型
     */
    private static Map<String, IStringSerialization> serializations = new HashMap<>();

    /**
     * 待序列化组件缓存
     * <p>
     * 主要用于反序列化时，寻找组件
     */
    private static Map<String, Class<? extends ECSModule>> moduleClassRecorder = new HashMap<>();
    private static Map<Class<? extends ECSModule>, String> moduleNameRecorder = new HashMap<>();

    /**
     * 緩存存檔
     * <p>
     * 用於插件不存在時存檔不丟
     */
    private static Map<Long, Map<String, String>> tempSaveData = new HashMap<>();

    /**
     * 导出所有模组
     *
     * @param gameObject
     * @return
     */
    public static Map<String, String> exportModuleData(GameObject gameObject) {
        return serializationECSModule(gameObject, gameObject.getModuleContainer().getModules());
    }

    /**
     * 导出单个模块
     *
     * @param gameObject
     * @param ecsModule
     * @return
     */
    public static Map.Entry<String, String> exportModuleData(GameObject gameObject, ECSModule ecsModule) {
        return serializationECSModule(gameObject, ecsModule);
    }

    /**
     * 导入模组数据
     *
     * @param gameObject
     * @param key
     * @param data
     * @return
     */
    public static boolean importModuleData(GameObject gameObject, String key, String data) {
        IStringSerialization stringSerialization = SerializationTool.serializations.get(key);
        if (stringSerialization == null) {
            // 若不包含 com.particle 表示是新的格式  緩存住
            if (!key.contains("com.particle")) {
                // 新格式的存檔在插件不存在時會遺失，故有以下邏輯緩存存檔
                Map<String, String> dataMap = tempSaveData.get(gameObject.ID);
                if (dataMap == null) {
                    dataMap = new HashMap<>();
                }

                dataMap.put(key, data);
                tempSaveData.put(gameObject.ID, dataMap);
            }

            return false;
        }

        Class<? extends ECSModule> moduleType = SerializationTool.moduleClassRecorder.get(key);
        if (moduleType == null) {
            return false;
        }

        ECSModule module = ECSModuleHandler.buildHandler(moduleType).bindModule(gameObject);
        stringSerialization.deserialization(gameObject, data, module);

        return true;
    }

    /**
     * 导入模组数据
     *
     * @param gameObject
     * @param data
     * @return
     */
    public static boolean importModuleData(GameObject gameObject, Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            boolean state = importModuleData(gameObject, entry.getKey(), entry.getValue());

            if (!state) {
                LOGGER.error("Fail to find deserialization methods for module key {}", entry.getKey());
                return false;
            }
        }

        return true;
    }

    // -------- 注册 ----------
    public static void registerSerializationWithModuleName(String key, IStringSerialization serialization, Class<? extends ECSModule> clazz) {
        registerSerializations(key, serialization);
        bindModuleName(key, clazz);
    }

    public static void registerSerializationWithModuleAliasName(String key, IStringSerialization serialization, Class<? extends ECSModule> clazz) {
        registerSerializations(key, serialization);
        bindModuleAliasName(key, clazz);
    }

    public static void registerSerializations(String key, IStringSerialization serialization) {
        SerializationTool.serializations.put(key, serialization);
    }

    public static void bindModuleName(String key, Class<? extends ECSModule> clazz) {
        SerializationTool.moduleClassRecorder.put(key, clazz);
        SerializationTool.moduleNameRecorder.put(clazz, key);
    }

    public static void bindModuleAliasName(String key, Class<? extends ECSModule> clazz) {
        SerializationTool.moduleClassRecorder.put(key, clazz);
    }

    private static Map<String, String> serializationECSModule(GameObject gameObject, List<ECSModule> ecsModules) {
        Map<String, String> results = new HashMap<>();

        for (ECSModule module : ecsModules) {
            String key = SerializationTool.moduleNameRecorder.get(module.getClass());
            if (key == null) {
                // LOGGER.error("Fail to find serialization key for module {}", module.getClass().getSimpleName());
                continue;
            }

            IStringSerialization stringSerialization = SerializationTool.serializations.get(key);
            if (stringSerialization == null) {
                LOGGER.error("Fail to find serialization methods for module key {}", key);
                continue;
            }

            results.put(key, stringSerialization.serialization(gameObject, module));
        }

        return results;
    }

    private static Map.Entry<String, String> serializationECSModule(GameObject gameObject, ECSModule module) {
        String key = SerializationTool.moduleNameRecorder.get(module.getClass());
        if (key == null) {
            // LOGGER.error("Fail to find serialization key for module {}", module.getClass().getSimpleName());
            return null;
        }

        IStringSerialization stringSerialization = SerializationTool.serializations.get(key);
        if (stringSerialization == null) {
            LOGGER.error("Fail to find serialization methods for module key {}", key);
            return null;
        }

        return new AbstractMap.SimpleEntry<>(key, stringSerialization.serialization(gameObject, module));
    }

    public static Map<Long, Map<String, String>> getTempSaveData() {
        return tempSaveData;
    }
}
