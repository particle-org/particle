package com.particle.game.entity.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.particle.game.entity.service.config.DropConfig;
import com.particle.game.entity.service.config.EquipmentConfig;
import com.particle.game.entity.service.config.MobEntityConfig;
import com.particle.model.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class MobEntityConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobEntityConfigService.class);

    private static final MobEntityConfig DEFAULT_MOB_ENTITY = new MobEntityConfig();

    private static final Map<String, MobEntityConfig> ENTITY_CONFIG_DATA = new HashMap<>();

    public static MobEntityConfig getMobEntityConfig(String entityType) {
        return ENTITY_CONFIG_DATA.getOrDefault(entityType, DEFAULT_MOB_ENTITY);
    }

    public static void loadMobEntityConfig() {
        // 读取配置文件
        File configDir = new File("config/entity/MobEntityConfig.csv");

        // 读取结果
        List<MobEntityConfig> configs = new ArrayList<>(40);
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
                if (splitData.size() != 17) {
                    LOGGER.error("Entity {} config data error!", splitData.size() > 0 ? splitData.get(0) : "Unamed!");
                    continue;
                }

                // 生物配置
                MobEntityConfig mobEntityConfig = new MobEntityConfig();
                mobEntityConfig.setEntity(splitData.get(0));
                mobEntityConfig.setHealth(Float.parseFloat(splitData.get(1)));
                mobEntityConfig.setArmor(Float.parseFloat(splitData.get(2)));
                mobEntityConfig.setDamage(Float.parseFloat(splitData.get(3)));
                mobEntityConfig.setBindBoxX(Float.parseFloat(splitData.get(4)));
                mobEntityConfig.setBindBoxY(Float.parseFloat(splitData.get(5)));
                mobEntityConfig.setBindBoxZ(Float.parseFloat(splitData.get(6)));
                mobEntityConfig.setBindBoxLengthX(Float.parseFloat(splitData.get(7)));
                mobEntityConfig.setBindBoxLengthY(Float.parseFloat(splitData.get(8)));
                mobEntityConfig.setBindBoxLengthZ(Float.parseFloat(splitData.get(9)));
                mobEntityConfig.setEyeHeight(Float.parseFloat(splitData.get(10)));
                mobEntityConfig.setSpeed(Float.parseFloat(splitData.get(11)));
                mobEntityConfig.setMaxSpeed(Float.parseFloat(splitData.get(12)));
                mobEntityConfig.setExperience(Integer.parseInt(splitData.get(13)));
                mobEntityConfig.setGravity(Float.parseFloat(splitData.get(14)));

                // 配置装备
                Map<String, EquipmentConfig> equipmentConfig = JSON.parseObject(splitData.get(15), (new TypeReference<Map<String, EquipmentConfig>>() {
                }));
                if (equipmentConfig == null) {
                    mobEntityConfig.setEquipment(new HashMap<>());
                } else {
                    for (Map.Entry<String, EquipmentConfig> equipmentEntrySet : equipmentConfig.entrySet()) {
                        equipmentEntrySet.getValue().setItemStack(ItemStack.getItem("minecraft:" + equipmentEntrySet.getKey(), 1));
                    }
                    mobEntityConfig.setEquipment(equipmentConfig);
                }


                // 配置掉落物
                Map<String, List<DropConfig>> dropsMap = JSON.parseObject(splitData.get(16), (new TypeReference<Map<String, List<DropConfig>>>() {
                }));
                if (dropsMap == null) {
                    mobEntityConfig.setDrops(new HashMap<>());
                } else {
                    for (Map.Entry<String, List<DropConfig>> dropsItemsConfig : dropsMap.entrySet()) {
                        for (DropConfig dropConfig : dropsItemsConfig.getValue()) {
                            dropConfig.setItemStack(ItemStack.getItem("minecraft:" + dropsItemsConfig.getKey(), dropConfig.getMeta(), dropConfig.getAmount()));
                        }
                    }
                    mobEntityConfig.setDrops(dropsMap);
                }


                configs.add(mobEntityConfig);
            }

        } catch (FileNotFoundException e) {
            LOGGER.error("Entity config file not founded!");
        } catch (IOException e) {
            LOGGER.error("Entity config file fail to read!");
        } catch (NumberFormatException e) {
            LOGGER.error(currentData);
            LOGGER.error("Illegal data", e);
        }

        for (MobEntityConfig config : configs) {
            ENTITY_CONFIG_DATA.put("minecraft:" + config.getEntity(), config);
        }
    }
}
