package com.particle.model.item;

import com.particle.model.item.types.ItemPrototype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ItemDisplayNamesDictionary {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemDisplayNamesDictionary.class);

    private static Map<ItemPrototype, String> itemNamesMap = new HashMap<>();
    private static Map<String, String> itemMetaNamesMap = new HashMap<>();

    static {
        loadItemNames();
        loadItemMetaNames();
    }

    public static String getName(ItemPrototype type) {
        String name = itemNamesMap.get(type);
        return name == null ? "" : name;
    }

    public static String getName(ItemPrototype type, byte meta) {
        String name = itemMetaNamesMap.get(type.getName() + ":" + meta);
        return name == null ? getName(type) : name;
    }

    private static void loadItemNames() {
        InputStream configFileStream = ItemDisplayNamesDictionary.class.getClassLoader().getResourceAsStream("item_name_dictionary.csv");

        if (configFileStream == null) {
            LOGGER.warn("Fail to load item name config file!");
            return;
        }

        try (BufferedReader itemNameReader = new BufferedReader(new InputStreamReader(configFileStream))) {
            String temp;

            while ((temp = itemNameReader.readLine()) != null) {
                // 过滤非法数据
                temp = temp.trim();
                if (temp.length() == 0) {
                    continue;
                }

                // 切分数据
                String[] split = temp.split(",");

                // 计算物品
                String key = split[0];
                if (key.startsWith("ItemPrototype.")) {
                    key = key.substring(14);
                }
                ItemPrototype itemPrototype = ItemPrototype.valueOf(key);

                if (split.length == 1) {
                    itemNamesMap.put(itemPrototype, key);
                } else {
                    itemNamesMap.put(itemPrototype, split[1]);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.error("item_name_dictionary config error!", e);
        }
    }

    private static void loadItemMetaNames() {
        InputStream configFileStream = ItemDisplayNamesDictionary.class.getClassLoader().getResourceAsStream("item_name_subid_dictionary.csv");

        if (configFileStream == null) {
            LOGGER.warn("Fail to load item meta name config file!");
            return;
        }

        try (BufferedReader itemNameReader = new BufferedReader(new InputStreamReader(configFileStream))) {
            String temp;

            while ((temp = itemNameReader.readLine()) != null) {
                // 过滤非法数据
                temp = temp.trim();
                if (temp.length() == 0) {
                    continue;
                }

                // 切分数据
                String[] split = temp.split(",");

                // 计算物品
                String key = split[0];
                if (key.startsWith("ItemPrototype.")) {
                    key = key.substring(14);
                }
                ItemPrototype itemPrototype = ItemPrototype.valueOf(key);

                if (split.length == 3) {
                    itemMetaNamesMap.put(itemPrototype.getName() + ":" + split[1], split[2]);
                } else {
                    LOGGER.warn("Item meta name config error, {}", temp);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.error("item_name_dictionary config error!", e);
        }
    }

}
