package com.particle.util.loader;

import java.util.List;
import java.util.Map;

public interface IConfigLoader {

    Map<String, Object> loadConfigs();

    List<Map<String, Object>> loadSchemas();

    void saveConfigs(Map<String, Object> configData, List<Map<String, Object>> schemas);
}
