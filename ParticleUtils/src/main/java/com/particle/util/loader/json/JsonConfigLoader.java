package com.particle.util.loader.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.particle.util.loader.IConfigLoader;
import com.particle.util.loader.StringFileOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonConfigLoader implements IConfigLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonConfigLoader.class);

    private File configFile;

    public JsonConfigLoader(String configFileName) {
        if (StringUtils.isBlank(configFileName)) {
            throw new RuntimeException("Illegal config file name");
        } else {
            this.configFile = new File(configFileName);
        }
    }

    public Map<String, Object> loadConfigs() {
        String jsonConfigs = StringFileOperator.readConfigFiles(this.configFile);
        if (jsonConfigs != null) {
            JSONObject jsonObject = JSON.parseObject(jsonConfigs);
            jsonObject.remove(".schema");
            return jsonObject;
        } else {
            return null;
        }
    }

    public List<Map<String, Object>> loadSchemas() {
        String jsonConfigs = StringFileOperator.readConfigFiles(this.configFile);
        if (jsonConfigs != null) {
            JSONObject jsonObject = JSON.parseObject(jsonConfigs);
            List<Object> schmaJsonData = jsonObject.getJSONArray(".schema");
            if (schmaJsonData != null) {
                List<Map<String, Object>> schemaList = new ArrayList(schmaJsonData.size());
                schmaJsonData.forEach((o) -> {
                    schemaList.add((JSONObject) o);
                });
                return schemaList;
            }
        }

        return null;
    }

    public void saveConfigs(Map<String, Object> configData, List<Map<String, Object>> schemas) {
        if (configData != null) {
            if (schemas != null) {
                configData.put(".schema", schemas);
            }

            StringFileOperator.writeConfigFiles(this.configFile, JSON.toJSONString(configData, new SerializerFeature[]{SerializerFeature.PrettyFormat, SerializerFeature.IgnoreNonFieldGetter}));
        }
    }
}
