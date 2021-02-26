package com.particle.network.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PotionMixJsonDataReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PotionMixJsonDataReader.class);

    public static JSONArray read(String url) {
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(BlockInfoReader.class.getClassLoader().getResourceAsStream(url)));
            String temp = null;
            while ((temp = bufferedReader.readLine()) != null) stringBuilder.append(temp);

            bufferedReader.close();
        } catch (IOException e) {
            LOGGER.error("Fail to load block info of {}", url, e);
        }

        return JSON.parseArray(stringBuilder.toString());
    }

}
