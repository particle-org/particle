package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.entity.component.farming.SaplingModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class SaplingTileEntity extends TileEntity {

    private static final ECSModuleHandler<PlantGrowUpProgressModule> PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);
    private static final ECSModuleHandler<SaplingModule> SAPLING_MODULE_HANDLER = ECSModuleHandler.buildHandler(SaplingModule.class);

    @Override
    public String getName() {
        return "sapling";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        // 种植物标志
        SAPLING_MODULE_HANDLER.bindModule(this);

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
