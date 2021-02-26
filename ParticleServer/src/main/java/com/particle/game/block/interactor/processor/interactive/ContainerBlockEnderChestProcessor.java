package com.particle.game.block.interactor.processor.interactive;

import com.particle.game.player.inventory.holder.BlockInventoryHolder;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.PlayerEnderChestInventory;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContainerBlockEnderChestProcessor extends BaseContainerBlockInteractedProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ContainerBlockEnderChestProcessor.class);

    @Override
    public boolean interactive(Player player, Block targetBlock, Vector3 targetPosition) {
        if (targetBlock == null || targetBlock.getType() != BlockPrototype.ENDER_CHEST) {
            logger.error("交互失败，目标方块不是末影箱");
            return false;
        }
        Inventory playerEnderChestInventory = this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ENDER);
        if (!(playerEnderChestInventory instanceof PlayerEnderChestInventory)) {
            logger.error("该玩家未初始化末影箱内容,或者末影箱类型错误！");
            return false;
        }
        playerEnderChestInventory.setInventoryHolder(new BlockInventoryHolder(playerEnderChestInventory, new Vector3f(targetPosition)));
        this.inventoryServiceProxy.addView(player, playerEnderChestInventory);
        this.inventoryManager.setOpenContainerStatus(player, ContainerType.ENDER_CHEST);

        return true;
    }
}
