package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.player.inventory.modules.ContainerViewerModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;

public class EnderTileEntity extends TileEntity {

    private static final ECSModuleHandler<ContainerViewerModule> CONTAINER_VIEWER_MODULE_HANDLER = ECSModuleHandler.buildHandler(ContainerViewerModule.class);

    @Override
    public String getName() {
        return "EnderChest";
    }

    @Override
    public boolean needNoticeClient() {
        return true;
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        CONTAINER_VIEWER_MODULE_HANDLER.bindModule(this);
    }

    @Override
    protected void onInit() {

    }
}
