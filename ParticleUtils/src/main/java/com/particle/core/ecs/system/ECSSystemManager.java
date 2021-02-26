package com.particle.core.ecs.system;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.utils.Bits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ECSSystemManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ECSSystemManager.class);

    private static final List<ECSSystemFactory> SYSTEM_LIST = new ArrayList<>();
    private static final Map<Integer, List<ECSSystemFactory>> MODULE_MAP = new HashMap<>();
    private static final Map<ECSSystemFactory, Bits> FINGERPRINT_CACHE = new HashMap<>();

    /**
     * 注册ECS系统
     *
     * @param ecsSystemFactory
     * @param <T>
     */
    public static <T extends ECSSystem> void registerSystem(ECSSystemFactory ecsSystemFactory) {
        // 缓存Service到系统的索引
        Class<? extends ECSModule>[] requestServices = ecsSystemFactory.getRequestServices();
        Bits fingerprint = new Bits();
        for (Class<? extends ECSModule> requestService : requestServices) {
            ECSModuleHandler<? extends ECSModule> ecsModuleHandler = ECSModuleHandler.buildHandler(requestService);

            // 获取业务索引
            Integer moduleIndex = ecsModuleHandler.getModuleIndex();

            // 标记对应业务的缓存
            List<ECSSystemFactory> cacheList = MODULE_MAP.computeIfAbsent(moduleIndex, k -> new ArrayList<>(4));
            cacheList.add(ecsSystemFactory);

            // 标记业务指纹缓存
            fingerprint.set(moduleIndex);
        }
        FINGERPRINT_CACHE.put(ecsSystemFactory, fingerprint);

        // 缓存系统
        SYSTEM_LIST.add(ecsSystemFactory);
    }

    /**
     * 构造Tick列表
     *
     * @param gameObject
     * @return
     */
    public static void buildECSSystemTickList(GameObject gameObject) {
        // 获取玩家持有的业务组件的指纹缓存
        Bits serviceFingerprint = gameObject.getModuleContainer().getServiceFingerprint();

        List<ECSSystem> ecsSystems = new ArrayList<>();

        // 遍历系统
        for (ECSSystemFactory systemFactory : SYSTEM_LIST) {
            // 获取该系统所需业务的指纹
            Bits systemFingerprint = FINGERPRINT_CACHE.get(systemFactory);

            // 如果GameObject持有该业务所需的所有组件
            if (serviceFingerprint.containsAll(systemFingerprint)) {
                ECSSystem ecsSystem = systemFactory.getOrBuildECSSystem(gameObject);

                if (ecsSystem != null) {
                    ecsSystems.add(ecsSystem);
                }
            }
        }

        gameObject.getSystemContainer().updateSystemList(ecsSystems);
    }

    /**
     * 获取系统Tick列表
     *
     * @param gameObject
     * @return
     */
    public static List<ECSSystem> getECSSystemTickList(GameObject gameObject) {
        return gameObject.getSystemContainer().getSystemList();
    }
}
