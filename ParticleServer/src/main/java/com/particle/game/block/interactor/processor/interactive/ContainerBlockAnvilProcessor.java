package com.particle.game.block.interactor.processor.interactive;

import com.particle.game.player.inventory.holder.BlockInventoryHolder;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.AnvilInventory;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class ContainerBlockAnvilProcessor extends BaseContainerBlockInteractedProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ContainerBlockAnvilProcessor.class);

    @Override
    public boolean interactive(Player player, Block targetBlock, Vector3 targetPosition) {
        // 如果目标block不是Anvil，直接返回
        if (targetBlock == null || targetBlock.getType() != BlockPrototype.ANVIL) {
            logger.error("交互失败，目标方块不是铁砧");
            return false;
        }
        AnvilInventory anvilInventory = new AnvilInventory();
        anvilInventory.setInventoryHolder(new BlockInventoryHolder(anvilInventory, new Vector3f(targetPosition)));
        this.inventoryManager.bindMultiInventory(player, anvilInventory);
        this.inventoryServiceProxy.addView(player, anvilInventory);
        this.inventoryManager.setOpenContainerStatus(player, ContainerType.ANVIL);
        return true;
    }
}
