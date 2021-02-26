package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.liquid.LavaModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class LavaTileEntity extends TileEntity {

    private static final ECSModuleHandler<LavaModule> LAVA_MODULE_HANDLER = ECSModuleHandler.buildHandler(LavaModule.class);

    @Override
    public String getName() {
        return "lava";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        LAVA_MODULE_HANDLER.bindModule(this);
    }

    @Override
    public boolean needNoticeClient() {
        return false;
    }

    @Override
    protected void onInit() {

    }
}