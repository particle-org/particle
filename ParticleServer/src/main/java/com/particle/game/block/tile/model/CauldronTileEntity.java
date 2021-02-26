package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.brewing.CauldronModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class CauldronTileEntity extends TileEntity {

    private static final ECSModuleHandler<CauldronModule> CAULDRON_MODULE_HANDLER = ECSModuleHandler.buildHandler(CauldronModule.class);

    @Override
    public String getName() {
        return "Cauldron";
    }

    @Override
    public boolean needNoticeClient() {
        return true;
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        CAULDRON_MODULE_HANDLER.bindModule(this);
    }

    @Override
    protected void onInit() {

    }
}
