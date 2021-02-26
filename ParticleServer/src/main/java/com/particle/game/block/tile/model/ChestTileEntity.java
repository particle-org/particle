package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.player.inventory.holder.EntityInventoryHolder;
import com.particle.game.player.inventory.modules.SingleContainerModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.inventory.ChestInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.math.Vector3;

public class ChestTileEntity extends TileEntity {

    private static final ECSModuleHandler<SingleContainerModule> SINGLE_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(SingleContainerModule.class);

    @Override
    public String getName() {
        return "Chest";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        //初始化ItemsContainerComponent
        Inventory inventory = new ChestInventory();
        inventory.setInventoryHolder(new EntityInventoryHolder(this, inventory));

        SingleContainerModule singleContainerModule = SINGLE_CONTAINER_MODULE_HANDLER.bindModule(this);
        singleContainerModule.setInventory(inventory);
    }

    @Override
    public boolean needNoticeClient() {
        return true;
    }

    @Override
    protected void onInit() {

    }
}
