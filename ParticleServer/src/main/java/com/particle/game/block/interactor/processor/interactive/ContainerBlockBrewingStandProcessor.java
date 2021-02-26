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
public class ContainerBlockBrewingStandProcessor extends BaseContainerBlockInteractedProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ContainerBlockBrewingStandProcessor.class);

    @Override
    public boolean interactive(Player player, Block targetBlock, Vector3 targetPosition) {
        if (targetBlock == null || targetBlock.getType() != BlockPrototype.BREWING_STAND) {
            logger.error("交互失败，目标方块不是酿造台");
            return false;
        }
        if (super.interactive(player, targetBlock, targetPosition)) {
            this.inventoryManager.setOpenContainerStatus(player, ContainerType.BREWING);
            return true;
        } else {
            return false;
        }
    }
}
