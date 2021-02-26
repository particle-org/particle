package com.particle.util.configer;

import com.google.inject.Provider;
import com.particle.util.configer.impl.PluginConfigManagerFileImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PluginConfigManagerProvider implements Provider<IPluginConfigManager> {

    private IPluginConfigManager configManager;

    @Inject
    private void init() {
        this.configManager = new PluginConfigManagerFileImpl();
    }

    @Override
    public IPluginConfigManager get() {
        return configManager;
    }
}
