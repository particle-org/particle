package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.common.modules.BannerModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class BannerTileEntity extends TileEntity {

    private static final ECSModuleHandler<BannerModule> BANNER_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(BannerModule.class);

    @Override
    public String getName() {
        return "Banner";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        BANNER_MODULE_ECS_MODULE_HANDLER.bindModule(this);
    }

    @Override
    public boolean needNoticeClient() {
        return true;
    }

    @Override
    protected void onInit() {

    }
}