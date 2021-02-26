package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.entity.component.farming.ReedsModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class ReedsTileEntity extends TileEntity {

    private static final ECSModuleHandler<PlantGrowUpProgressModule> PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);
    private static final ECSModuleHandler<ReedsModule> REEDS_MODULE_HANDLER = ECSModuleHandler.buildHandler(ReedsModule.class);


    @Override
    public String getName() {
        return "Reeds";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        REEDS_MODULE_HANDLER.bindModule(this);
        PROGRESS_MODULE_HANDLER.bindModule(this);
    }

    @Override
    public boolean needNoticeClient() {
        return false;
    }

    @Override
    protected void onInit() {

    }
}
