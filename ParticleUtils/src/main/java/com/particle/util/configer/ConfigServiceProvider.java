package com.particle.util.configer;

import com.particle.util.configer.impl.BeautifyJsonFileConfigService;

public class ConfigServiceProvider {
    private static final IConfigService INSTANCE = new BeautifyJsonFileConfigService("config");

    public static IConfigService getConfigService() {
        return ConfigServiceProvider.INSTANCE;
    }
}
