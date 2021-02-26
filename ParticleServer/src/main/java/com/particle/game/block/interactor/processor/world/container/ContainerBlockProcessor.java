package com.particle.game.block.interactor.processor.world.container;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.interactor.processor.world.DirectionBlockProcessor;
import com.particle.game.player.inventory.modules.SingleContainerModule;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.block.Block;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;

public abstract class ContainerBlockProcessor extends DirectionBlockProcessor {

    private static final ECSModuleHandler<SingleContainerModule> SINGLE_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(SingleContainerModule.class);

    @Inject
    private InventoryAPIProxy inventoryAPIProxy;

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 position) {
        TileEntity tileEntity = this.tileEntityService.getEntityAt(level, position);

        if (tileEntity == null) {
            return false;
        }

        SingleContainerModule singleContainerModule = SINGLE_CONTAINER_MODULE_HANDLER.getModule(tileEntity);
        if (singleContainerModule == null) {
            return false;
        }

        // 关闭所有玩家界面
        for (Player viewer : singleContainerModule.getInventory().getViewers()) {
            this.inventoryAPIProxy.removeView(viewer, singleContainerModule.getInventory(), true);
        }

        // 掉落箱子物品
        singleContainerModule.getInventory().getAllSlots().forEach((integer, itemStack) -> {
            //配置掉落物
            ItemEntity itemEntity = itemEntityService.createEntity(itemStack, new Vector3f(position));

            this.entitySpawnService.spawn(level, itemEntity);
        });

        // 清空箱子物品
        this.inventoryAPIProxy.clearAll(singleContainerModule.getInventory());

        return super.onBlockPreDestroy(level, player, targetBlock, position);
    }
}
