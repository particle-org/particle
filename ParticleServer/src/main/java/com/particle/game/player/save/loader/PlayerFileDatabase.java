package com.particle.game.player.save.loader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.particle.api.player.PlayerDatabaseApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class PlayerFileDatabase implements PlayerDatabaseApi {
    private static final Logger log = LoggerFactory.getLogger(PlayerFileDatabase.class);

    private File dir;

    @Inject
    public void init() {
        this.dir = new File("players/");
        if (!this.dir.exists()) {
            this.dir.mkdir();
        }
    }

    @Override
    public void savePlayerData(String uuid, String name, Map<String, String> values, Map<String, String> ecsData, boolean release) {
        File file = new File(this.dir, uuid + ".json");
        try {
            Map<String, String> saveData = Maps.newHashMap();
            saveData.put("uuid", uuid);
            saveData.put("name", name);

            for (Map.Entry<String, String> entry : values.entrySet()) {
                saveData.put(entry.getKey(), entry.getValue());
            }

            saveData.put("ecs", JSON.toJSONString(ecsData, SerializerFeature.PrettyFormat));

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

            bufferedWriter.write(JSON.toJSONString(saveData, SerializerFeature.PrettyFormat));
            bufferedWriter.flush();

            bufferedWriter.close();
        } catch (Exception e) {
            log.error("Fail to save entity {} data!", uuid);
        }
    }

    @Override
    public Map<String, String> loadPlayerDataByUUID(String uuid) {
        File file = new File(this.dir, uuid + ".json");

        if (file.exists()) {
            try {
                // 读取存档数据
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                StringBuilder saveDataBuilder = new StringBuilder();
                String temp = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    saveDataBuilder.append(temp);
                }

                bufferedReader.close();

                // JSONObject转换
                JSONObject jsonObject = JSON.parseObject(saveDataBuilder.toString());
                Map<String, String> resultData = new HashMap<>();
                for (String key : jsonObject.keySet()) {
                    if (key.equals("uuid") || key.equals("locker") || key.equals("_id") || key.equals("name") || key.equals("ecs")) {
                        continue;
                    }
                    resultData.put(key, jsonObject.getString(key));
                }

                return resultData;
            } catch (Exception e) {
                log.error("Fail to save entity {} data!", uuid);
            }
        } else {
            log.debug("Player {} data not found!", uuid);
        }

        return null;
    }

    @Override
    public Map<String, String> loadPlayerECSDataByUUID(String uuid) {
        File file = new File(this.dir, uuid + ".json");

        if (file.exists()) {
            try {
                // 读取存档数据
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                StringBuilder saveDataBuilder = new StringBuilder();
                String temp = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    saveDataBuilder.append(temp);
                }
                bufferedReader.close();

                // JSONObject转换
                JSONObject jsonObject = JSON.parseObject(saveDataBuilder.toString()).getJSONObject("ecs");
                Map<String, String> resultData = new HashMap<>();
                for (String key : jsonObject.keySet()) {
                    resultData.put(key, jsonObject.getString(key));
                }

                return resultData;
            } catch (Exception e) {
                log.error("Fail to save entity {} data!", uuid);
            }
        } else {
            log.debug("Player {} data not found!", uuid);
        }

        return null;
    }

    @Override
    public void removePlayerData(String uuid, String keys) {

    }

    @Override
    public void removePlayerDataByUUID(String uuid) {
        File file = new File(this.dir, uuid + ".json");

        if (file.exists()) {
            try {
                file.delete();
            } catch (Exception e) {
                log.error("Fail to remove entity {} data!", uuid);
            }
        } else {
            log.info("Player {} data not found!", uuid);
        }
    }
}
