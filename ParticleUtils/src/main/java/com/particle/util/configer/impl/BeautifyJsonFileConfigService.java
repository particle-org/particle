package com.particle.util.configer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.particle.util.configer.anno.ConfigBean;
import com.particle.util.configer.exception.ConfigBeanNotDetectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class BeautifyJsonFileConfigService extends JsonFileConfigService {
    private static final Logger logger = LoggerFactory.getLogger(BeautifyJsonFileConfigService.class);

    private File dir;

    public BeautifyJsonFileConfigService(String dir) {
        super(dir);

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
            String savedData = JSON.toJSONString(t, SerializerFeature.PrettyFormat);

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
            String savedData = JSON.toJSONString(t, SerializerFeature.PrettyFormat);

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

}
