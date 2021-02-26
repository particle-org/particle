package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.liquid.WaterModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class WaterTileEntity extends TileEntity {

    private static final ECSModuleHandler<WaterModule> WATER_MODULE_HANDLER = ECSModuleHandler.buildHandler(WaterModule.class);

    @Override
    public String getName() {
        return "water";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        WATER_MODULE_HANDLER.bindModule(this);
    }

    @Override
    public boolean needNoticeClient() {
        return false;
    }

    @Override
    protected void onInit() {

    }
}