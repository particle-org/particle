package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModule;

public class TileEntityConfig {

    private String name;
    private boolean clientSide;
    private Class<? extends ECSModule>[] bindModules;

    public TileEntityConfig(String name, boolean clientSide, Class<? extends ECSModule>... bindModules) {
        this.name = name;
        this.clientSide = clientSide;
        this.bindModules = bindModules;
    }

    public String getName() {
        return name;
    }

    public boolean isClientSide() {
        return clientSide;
    }

    public Class<? extends ECSModule>[] getBindModules() {
        return bindModules;
    }
}
