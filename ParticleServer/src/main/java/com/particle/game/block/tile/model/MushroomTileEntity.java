package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.farming.MushroomModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class MushroomTileEntity extends TileEntity {

    private static final ECSModuleHandler<MushroomModule> MUSHROOM_MODULE_HANDLER = ECSModuleHandler.buildHandler(MushroomModule.class);


    @Override
    public String getName() {
        return "Mushroom";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        MUSHROOM_MODULE_HANDLER.bindModule(this);
    }

    @Override
    public boolean needNoticeClient() {
        return false;
    }

    @Override
    protected void onInit() {

    }
}
