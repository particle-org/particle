package com.particle.game.entity.service;

import com.particle.game.entity.service.config.MonsterEntityConfig;
import com.particle.model.entity.type.EntityTypeDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class MonsterEntityConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonsterEntityConfigService.class);

    private static final MonsterEntityConfig DEFAULT_MONSTER_ENTITY = new MonsterEntityConfig();

    private static final Map<String, MonsterEntityConfig> ENTITY_CONFIG_DATA = new HashMap<>();

    public static MonsterEntityConfig getMonsterEntityConfig(String entityType) {
        return ENTITY_CONFIG_DATA.getOrDefault(entityType, DEFAULT_MONSTER_ENTITY);
    }

    public static void init() {
        loadMonsterEntityConfig();
    }

    private static void loadMonsterEntityConfig() {
        // 读取配置文件
        File configDir = new File("config/entity/MonsterEntityConfig.csv");

        // 读取结果
        List<MonsterEntityConfig> configs = new ArrayList<>(40);
        String currentData = null;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(configDir)));

            // 读取数据，跳过第一行
            String data = bufferedReader.readLine();
            while ((data = bufferedReader.readLine()) != null) {
                currentData = data;

                List<String> splitData = new LinkedList<>();

                // 解析配置文件
                while (data.length() > 0) {
                    String evalData;

                    if (data.startsWith("\"")) {
                        int splitIndex = data.indexOf("\"", 1);
                        evalData = data.substring(1, splitIndex);

                        data = data.substring(splitIndex + 1);

                        if (data.length() > 0) {
                            data = data.substring(1);
                        }
                    } else {
                        int splitIndex = data.indexOf(",");
                        if (splitIndex == -1) {
                            evalData = data;
                            data = "";
                        } else {
                            evalData = data.substring(0, splitIndex);
                            data = data.substring(splitIndex + 1);
                        }

                    }

                    splitData.add(evalData);
                }

                // 读取数据
                if (splitData.size() != 9) {
                    LOGGER.error("Entity {} config data error!", splitData.size() > 0 ? splitData.get(0) : "Unamed!");
                    continue;
                }

                // 生物配置
                MonsterEntityConfig monsterEntityConfig = new MonsterEntityConfig();
                monsterEntityConfig.setEntity(splitData.get(0));
                monsterEntityConfig.setHealth(Float.parseFloat(splitData.get(1)));
                monsterEntityConfig.setDamage(Float.parseFloat(splitData.get(2)));
                monsterEntityConfig.setSpeed(Float.parseFloat(splitData.get(3)));
                monsterEntityConfig.setMaxSpeed(Float.parseFloat(splitData.get(4)));
                monsterEntityConfig.setExperience(Integer.parseInt(splitData.get(5)));
                monsterEntityConfig.setGravity(Float.parseFloat(splitData.get(6)));
                monsterEntityConfig.setWidth(Float.parseFloat(splitData.get(7)));
                monsterEntityConfig.setHeight(Float.parseFloat(splitData.get(8)));

                configs.add(monsterEntityConfig);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Entity config file not founded!");
        } catch (IOException e) {
            LOGGER.error("Entity config file fail to read!");
        } catch (NumberFormatException e) {
            LOGGER.error(currentData);
            LOGGER.error("Illegal data", e);
        }

        int type = 1000;
        for (MonsterEntityConfig config : configs) {
            String actorType = String.format("monster:%s", config.getEntity());
            ENTITY_CONFIG_DATA.put(actorType, config);
            // 注册自定义生物
            EntityTypeDictionary.registerEntityType(++type, actorType, false, false, false, ":");
        }
    }
}
