package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.farming.CocoaModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class CocoaTileEntity extends TileEntity {

    private static final ECSModuleHandler<CocoaModule> COCOA_MODULE_HANDLER = ECSModuleHandler.buildHandler(CocoaModule.class);


    @Override
    public String getName() {
        return "cocoa_title_entity";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        COCOA_MODULE_HANDLER.bindModule(this);
    }

    @Override
    public boolean needNoticeClient() {
        return false;
    }

    @Override
    protected void onInit() {

    }
}
