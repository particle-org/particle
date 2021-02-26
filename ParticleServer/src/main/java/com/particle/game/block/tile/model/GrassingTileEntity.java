package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.planting.components.GrassSeparateModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class GrassingTileEntity extends TileEntity {

    private static final ECSModuleHandler<GrassSeparateModule> GRASS_SEPARATE_MODULE_HANDLER = ECSModuleHandler.buildHandler(GrassSeparateModule.class);

    @Override
    public String getName() {
        return "Grass";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        GRASS_SEPARATE_MODULE_HANDLER.bindModule(this);
    }

    @Override
    public boolean needNoticeClient() {
        return false;
    }

    @Override
    protected void onInit() {

    }
}
