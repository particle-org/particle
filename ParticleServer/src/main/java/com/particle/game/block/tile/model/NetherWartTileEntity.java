package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.farming.NetherWartModule;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class NetherWartTileEntity extends TileEntity {

    private static final ECSModuleHandler<PlantGrowUpProgressModule> PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);
    private static final ECSModuleHandler<NetherWartModule> NETHER_WART_MODULE_HANDLER = ECSModuleHandler.buildHandler(NetherWartModule.class);

    @Override
    public String getName() {
        return "netherWart";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        // 种地獄疙瘩标志
        NetherWartModule netherWartModule = NETHER_WART_MODULE_HANDLER.bindModule(this);
        netherWartModule.setMaxGrowMeta(3);
        netherWartModule.setRemoveTileEntityAfterMature(true);

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
