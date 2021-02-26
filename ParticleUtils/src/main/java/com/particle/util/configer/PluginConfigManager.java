package com.particle.util.configer;

import com.google.inject.Singleton;
import com.particle.util.configer.impl.BeautifyJsonFileConfigService;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class PluginConfigManager {

    private static final String CONFIG_DIR = "plugins/";

    private Map<String, IConfigService> configServiceMap = new HashMap<>();

    public IConfigService getConfigService(String namespace) {
        return new BeautifyJsonFileConfigService(CONFIG_DIR + namespace);
    }

}
