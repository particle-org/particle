package com.particle.network.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.particle.network.handler.common.ItemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ItemInfoReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemInfoReader.class);

    public static List<ItemInfo> read(String url) {
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ItemInfoReader.class.getClassLoader().getResourceAsStream(url)));
            String temp = null;
            while ((temp = bufferedReader.readLine()) != null) stringBuilder.append(temp);

            bufferedReader.close();
        } catch (IOException e) {
            LOGGER.error("Fail to load item info of {}", url, e);
        }

        return JSON.parseObject(stringBuilder.toString(), new TypeReference<List<ItemInfo>>() {
        });
    }

}
