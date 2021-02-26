package com.particle.model.events.level.plugin;

import com.particle.model.events.level.LevelEvent;
import com.particle.model.level.Level;

public class PluginConfigUpdateEvent extends LevelEvent {

    public static final String TARGET_MESSAGE = "message";

    public static final String TARGET_CONFIG = "config";

    private String plugin = "all";

    private String target = TARGET_CONFIG;


    public PluginConfigUpdateEvent(Level level) {
        super(level);
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
