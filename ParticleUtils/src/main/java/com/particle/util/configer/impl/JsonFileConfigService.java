package com.particle.util.configer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.particle.util.configer.IConfigService;
import com.particle.util.configer.anno.ConfigBean;
import com.particle.util.configer.exception.ConfigBeanNotDetectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class JsonFileConfigService implements IConfigService {
    private static final Logger logger = LoggerFactory.getLogger(JsonFileConfigService.class);

    private File dir;

    public JsonFileConfigService(String dir) {
        this.dir = new File(dir);
        if (!this.dir.exists()) {
            this.dir.mkdirs();
        }
    }

    @Override
    public <T> boolean saveConfig(T t) {
        ConfigBean configBeanDetail = t.getClass().getDeclaredAnnotation(ConfigBean.class);
        if (configBeanDetail == null) {
            throw new ConfigBeanNotDetectedException();
        }

        File file = new File(this.dir, configBeanDetail.name() + ".json");

        try {
            String savedData = JSON.toJSONString(t);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

            bufferedWriter.write(savedData);
            bufferedWriter.flush();

            bufferedWriter.close();

            return true;
        } catch (Exception e) {
            logger.error("Fail to save config {}!", configBeanDetail.name());
        }

        return false;
    }

    @Override
    public <T> T loadConfig(Class<T> clazz) {
        ConfigBean configBeanDetail = clazz.getDeclaredAnnotation(ConfigBean.class);
        if (configBeanDetail == null) {
            throw new ConfigBeanNotDetectedException();
        }

        File file = new File(this.dir, configBeanDetail.name() + ".json");

        if (file.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                String line = null;
                StringBuilder savedData = new StringBuilder("");
                while ((line = bufferedReader.readLine()) != null) {
                    savedData.append(line);
                }

                bufferedReader.close();

                return JSON.parseObject(savedData.toString(), clazz);
            } catch (Exception e) {
                logger.error("Fail to save Config {}!", configBeanDetail.name());
            }
        } else {
            String fileName = configBeanDetail.name();
            logger.warn("config: {}.json file not found, Automatically generate {}.json", fileName, fileName);
        }

        return null;
    }

    @Override
    public <T> boolean saveConfigs(List<T> t) {
        if (t.size() == 0) {
            return false;
        }

        ConfigBean configBeanDetail = t.get(0).getClass().getDeclaredAnnotation(ConfigBean.class);
        if (configBeanDetail == null) {
            throw new ConfigBeanNotDetectedException();
        }

        File file = new File(this.dir, configBeanDetail.name() + ".json");

        try {
            String savedData = JSON.toJSONString(t);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

            bufferedWriter.write(savedData);
            bufferedWriter.flush();

            bufferedWriter.close();

            return true;
        } catch (Exception e) {
            logger.error("Fail to save config {}!", configBeanDetail.name());
        }

        return false;
    }

    @Override
    public <T> List<T> loadConfigs(Class<T> clazz) {
        ConfigBean configBeanDetail = clazz.getDeclaredAnnotation(ConfigBean.class);
        if (configBeanDetail == null) {
            throw new ConfigBeanNotDetectedException();
        }

        File file = new File(this.dir, configBeanDetail.name() + ".json");

        if (file.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                String line = null;
                StringBuilder savedData = new StringBuilder("");
                while ((line = bufferedReader.readLine()) != null) {
                    savedData.append(line);
                }

                bufferedReader.close();

                return JSONArray.parseArray(savedData.toString(), clazz);
            } catch (Exception e) {
                // 如果发现配置错误，直接不允许启服
                String errorInfo = String.format("Fail to save Config %s", configBeanDetail.name());
                logger.error(errorInfo, e);
                System.exit(1);
            }
        } else {
            String fileName = configBeanDetail.name();
            logger.warn("config: {}.json file not found, Automatically generate {}.json", fileName, fileName);
        }

        return null;
    }
}
