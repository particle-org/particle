package com.particle.network.utils;

import com.alibaba.fastjson.JSON;
import com.particle.model.nbt.NBTTagList;
import com.particle.model.nbt.NBTToJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BlockNBTJsonReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockNBTJsonReader.class);

    public static NBTTagList read(String url) {
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(BlockNBTJsonReader.class.getClassLoader().getResourceAsStream(url)));
            String temp = null;
            while ((temp = bufferedReader.readLine()) != null) stringBuilder.append(temp);

            bufferedReader.close();
        } catch (IOException e) {
            LOGGER.error("Fail to load block info of {}", url, e);
        }

        return (NBTTagList) NBTToJsonObject.convertToTag(JSON.parseObject(stringBuilder.toString()));
    }
}
