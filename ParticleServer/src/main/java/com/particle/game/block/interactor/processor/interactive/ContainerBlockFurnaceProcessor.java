package com.particle.game.block.interactor.processor.interactive;

import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class ContainerBlockFurnaceProcessor extends BaseContainerBlockInteractedProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ContainerBlockFurnaceProcessor.class);

    @Override
    public boolean interactive(Player player, Block targetBlock, Vector3 targetPosition) {
        if (targetBlock == null || targetBlock.getType() != BlockPrototype.FURNACE
                && targetBlock.getType() != BlockPrototype.LIT_FURNACE) {
            logger.error("目标方块为空！");
            return false;
        }

        if (super.interactive(player, targetBlock, targetPosition)) {
            this.inventoryManager.setOpenContainerStatus(player, ContainerType.FURNACE);
            return true;
        } else {
            return false;
        }
    }
}
