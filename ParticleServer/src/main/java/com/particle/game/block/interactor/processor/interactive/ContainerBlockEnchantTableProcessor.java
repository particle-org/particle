package com.particle.game.block.interactor.processor.interactive;

import com.particle.game.player.inventory.holder.BlockInventoryHolder;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.EnchantInventory;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class ContainerBlockEnchantTableProcessor extends BaseContainerBlockInteractedProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ContainerBlockEnchantTableProcessor.class);

    @Override
    public boolean interactive(Player player, Block targetBlock, Vector3 targetPosition) {
        // 如果目标block不是enchantTable，直接返回
        if (targetBlock == null || targetBlock.getType() != BlockPrototype.ENCHANTING_TABLE) {
            logger.error("交互失败，目标方块不是附魔台");
            return false;
        }
        EnchantInventory enchantInventory = new EnchantInventory();

        enchantInventory.setInventoryHolder(new BlockInventoryHolder(enchantInventory, new Vector3f(targetPosition)));
        this.inventoryManager.bindMultiInventory(player, enchantInventory);
        this.inventoryServiceProxy.addView(player, enchantInventory);
        this.inventoryManager.setOpenContainerStatus(player, ContainerType.ENCHANT_TABLE);

        return true;
    }
}
