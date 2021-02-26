package com.particle.game.utils.logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.particle.api.utils.IPlayerLogData;
import com.particle.api.utils.IPlayerLogServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.network.NetworkInfo;
import com.particle.model.network.NetworkService;
import com.particle.model.player.Player;
import com.particle.util.log.HiveLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class PlayerLogService implements IPlayerLogServiceApi {

    private static final ECSModuleHandler<PlayerLogDataCacheModule> PLAYER_LOG_DATA_CACHE_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerLogDataCacheModule.class);

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerLogService.class);

    @Inject
    private NetworkService networkService;

    private String serverId = "Undefined";

    public void initServerId() {
        NetworkInfo networkInfo = this.networkService.getNetworkInfo();

        if (networkInfo != null) {
            this.serverId = String.format("%s:%s", networkInfo.getIp(), networkInfo.getPort());
        }
    }

    @Override
    public void log(Player player, String tag, Map.Entry<String, Object>... logData) {
        if (this.serverId.equals("Undefined")) {
            this.initServerId();
        }

        Map<String, Object> combined = new HashMap<>();

        // 放入自定义数据
        for (Map.Entry<String, Object> logDatum : logData) {
            combined.put(logDatum.getKey(), logDatum.getValue());
        }

        // 放入综合数据
        this.putGenerateData(player, combined);

        HiveLogger.log(tag, JSON.toJSONString(combined));
    }

    @Override
    public void log(Player player, String tag, Map<String, Object> logData) {
        if (this.serverId.equals("Undefined")) {
            this.initServerId();
        }

        // 放入自定义数据
        Map<String, Object> combined = new HashMap<>(logData);

        // 放入综合数据
        this.putGenerateData(player, combined);

        HiveLogger.log(tag, JSON.toJSONString(combined));
    }

    @Override
    public <T extends IPlayerLogData> void log(Player player, String tag, T objectBean) {
        if (this.serverId.equals("Undefined")) {
            this.initServerId();
        }

        try {
            // 放入自定义数据
            Map<String, Object> combined = (JSONObject) JSON.toJSON(objectBean);

            // 放入综合数据
            this.putGenerateData(player, combined);

            HiveLogger.log(tag, JSON.toJSONString(combined));
        } catch (Exception e) {
            LOGGER.error("Fail to log player {} data {}!", player.getIdentifiedStr(), tag, e);
        }
    }

    private void putGenerateData(Player player, Map<String, Object> combined) {
        combined.put("server", this.serverId);

        PlayerLogDataCacheModule playerLogDataCacheModule = PLAYER_LOG_DATA_CACHE_MODULE_ECS_MODULE_HANDLER.getModule(player);
        if (playerLogDataCacheModule == null) {
            LOGGER.error("Fail to log player {} because player log data cache module not initec!", player.getIdentifiedStr());
            return;
        }

        combined.put("uuid", playerLogDataCacheModule.getUuid());
        combined.put("udid", playerLogDataCacheModule.getUdid());
        combined.put("rold_id", playerLogDataCacheModule.getRoleId());
        combined.put("role_name", playerLogDataCacheModule.getRoleName());
        combined.put("address", playerLogDataCacheModule.getAddress());
        combined.put("port", playerLogDataCacheModule.getPort());
    }

    private static Map<String, Object> object2Map(Object obj) throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }

        Class<?> fileClass = obj.getClass();

        while (fileClass != Object.class) {
            for (Field field : fileClass.getDeclaredFields()) {
                String fieldName = field.getName();
                String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = null;
                try {
                    method = fileClass.getMethod(methodName);
                } catch (NoSuchMethodException e) {
                    continue;
                }

                if (method != null && (method.getModifiers() & 1) != 0) {
                    Object fileData = method.invoke(obj);
                    if (fileClass.isPrimitive()
                            || fileData instanceof String
                            || fileData instanceof Integer
                            || fileData instanceof Long
                            || fileData instanceof Double
                            || fileData instanceof BigDecimal
                            || fileData instanceof Float
                            || fileData instanceof Boolean
                    ) {
                        map.put(field.getName(), fileData);
                    } else {
                        map.put(field.getName(), object2Map(fileData));
                    }
                }
            }

            fileClass = fileClass.getSuperclass();
        }

        return map;
    }
}
