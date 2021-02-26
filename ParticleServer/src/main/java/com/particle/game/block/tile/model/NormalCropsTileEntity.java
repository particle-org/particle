package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.farming.CropsModule;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class NormalCropsTileEntity extends TileEntity {

    private static final ECSModuleHandler<PlantGrowUpProgressModule> PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);
    private static final ECSModuleHandler<CropsModule> CROPS_MODULE_HANDLER = ECSModuleHandler.buildHandler(CropsModule.class);

    @Override
    public String getName() {
        return "normalCrops";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        // 种植物标志
        CropsModule cropsModule = CROPS_MODULE_HANDLER.bindModule(this);
        cropsModule.setMaxGrowMeta(7);
        cropsModule.setRemoveTileEntityAfterMature(true);


        // 生长控制
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
