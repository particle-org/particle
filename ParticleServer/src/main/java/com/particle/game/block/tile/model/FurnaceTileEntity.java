package com.particle.game.block.tile.model;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.common.modules.CookModule;
import com.particle.game.block.furnace.FurnaceFuelModule;
import com.particle.game.player.inventory.holder.EntityInventoryHolder;
import com.particle.game.player.inventory.modules.SingleContainerModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.inventory.FurnaceInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.math.Vector3;

public class FurnaceTileEntity extends TileEntity {

    private static final ECSModuleHandler<CookModule> COOK_MODULE_HANDLER = ECSModuleHandler.buildHandler(CookModule.class);
    private static final ECSModuleHandler<FurnaceFuelModule> FURNACE_FUEL_MODULE_HANDLER = ECSModuleHandler.buildHandler(FurnaceFuelModule.class);
    private static final ECSModuleHandler<SingleContainerModule> SINGLE_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(SingleContainerModule.class);


    @Override
    public String getName() {
        return "Furnace";
    }

    @Override
    public void onCreated(Vector3 position) {
        super.onCreated(position);

        //初始化FurnaceItemContainerComponent
        Inventory inventory = new FurnaceInventory();
        inventory.setInventoryHolder(new EntityInventoryHolder(this, inventory));

        SingleContainerModule singleContainerModule = SINGLE_CONTAINER_MODULE_HANDLER.bindModule(this);
        singleContainerModule.setInventory(inventory);

        CookModule cookModule = COOK_MODULE_HANDLER.bindModule(this);
        cookModule.setCookTime(0);

        FURNACE_FUEL_MODULE_HANDLER.bindModule(this);
    }

    @Override
    public boolean needNoticeClient() {
        return true;
    }

    @Override
    protected void onInit() {

    }
}
