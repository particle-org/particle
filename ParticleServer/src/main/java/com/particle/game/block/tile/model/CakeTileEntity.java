package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.farming.CakeModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class CakeTileEntity extends TileEntity {

    private static final ECSModuleHandler<CakeModule> CAKE_MODULE_HANDLER = ECSModuleHandler.buildHandler(CakeModule.class);


    @Override
    public String getName() {
        return "cake_tile_entity";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        CAKE_MODULE_HANDLER.bindModule(this);
    }

    @Override
    public boolean needNoticeClient() {
        return false;
    }

    @Override
    protected void onInit() {

    }
}
