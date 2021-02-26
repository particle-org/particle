package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.common.modules.SkullModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class SkullTileEntity extends TileEntity {

    private static final ECSModuleHandler<SkullModule> SKULL_MODULE_HANDLER = ECSModuleHandler.buildHandler(SkullModule.class);

    @Override
    public String getName() {
        return "Skull";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        SKULL_MODULE_HANDLER.bindModule(this);
    }

    @Override
    public boolean needNoticeClient() {
        return true;
    }

    @Override
    protected void onInit() {

    }
}