package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.farming.CactusModule;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class CactusTileEntity extends TileEntity {

    private static final ECSModuleHandler<CactusModule> CACTUS_MODULE_HANDLER = ECSModuleHandler.buildHandler(CactusModule.class);
    private static final ECSModuleHandler<PlantGrowUpProgressModule> PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);

    @Override
    public String getName() {
        return "Cactus";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        CACTUS_MODULE_HANDLER.bindModule(this);
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
