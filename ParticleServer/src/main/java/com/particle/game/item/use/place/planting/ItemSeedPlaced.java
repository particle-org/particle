package com.particle.game.item.use.place.planting;

import com.particle.game.item.use.place.ItemPlaceBlockProcessor;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * 种子生长
 */
@Singleton
public class ItemSeedPlaced extends ItemPlaceBlockProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ItemSeedPlaced.class);

    @Override
    protected Block getBlock(Player player, ItemUseInventoryData itemUseInventoryData) {
        ItemStack handItem = itemUseInventoryData.getItem();
        if (handItem == null) {
            return null;
        }
        Block block = null;
        switch (handItem.getItemType()) {
            case WHEAT_SEEDS:
                block = Block.getBlock(BlockPrototype.WHEAT);
                break;
            case CARROT:
                block = Block.getBlock(BlockPrototype.CARROTS);
                break;
            case POTATO:
                block = Block.getBlock(BlockPrototype.POTATOES);
                break;
            case BEETROOT_SEEDS:
                block = Block.getBlock(BlockPrototype.BEETROOT);
                break;
            case MELON_SEEDS:
                block = Block.getBlock(BlockPrototype.MELON_STEM);
                break;
            case PUMPKIN_SEEDS:
                block = Block.getBlock(BlockPrototype.PUMPKIN_STEM);
                break;
        }
        if (block == null) {
            return null;
        }
        block.setMeta(0);
        return block;
    }
}
