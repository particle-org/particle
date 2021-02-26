package com.particle.game.block.interactor.processor.interactive;

import com.particle.api.block.IBlockInteractedProcessor;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.modules.SingleContainerModule;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.block.Block;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.events.level.player.PlayerOpenBlockInventoryEvent;
import com.particle.model.inventory.Inventory;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;

public abstract class BaseContainerBlockInteractedProcessor implements IBlockInteractedProcessor {

    private static final ECSModuleHandler<SingleContainerModule> SINGLE_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(SingleContainerModule.class);

    @Inject
    protected InventoryManager inventoryManager;

    @Inject
    protected InventoryAPIProxy inventoryServiceProxy;

    @Inject
    private TileEntityService tileEntityService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    /**
     * 当目标方块被交互
     *
     * @param player
     * @param targetBlock
     * @param targetPosition
     * @return true表示可以继续交互，false表示交互取消
     */
    @Override
    public boolean interactive(Player player, Block targetBlock, Vector3 targetPosition) {
        TileEntity entityAt = tileEntityService.getEntityAt(player.getLevel(), targetPosition);

        // 如果目标位置没有TileEntity，则直接返回false
        if (entityAt == null) {
            return false;
        }

        SingleContainerModule singleContainerModule = SINGLE_CONTAINER_MODULE_HANDLER.getModule(entityAt);

        if (singleContainerModule != null) {
            Inventory inventory = singleContainerModule.getInventory();

            if (inventory != null) {
                PlayerOpenBlockInventoryEvent event =
                        new PlayerOpenBlockInventoryEvent(player, player.getLevel());
                event.setBlock(targetBlock);
                event.setBlockPosition(targetPosition);
                event.setInventory(inventory);
                event.overrideAfterEventExecuted(() -> {
                    this.inventoryManager.bindMultiInventory(player, inventory);
                    this.inventoryServiceProxy.addView(player, inventory);
                });

                eventDispatcher.dispatchEvent(event);
            }
        }

        return true;
    }
}
