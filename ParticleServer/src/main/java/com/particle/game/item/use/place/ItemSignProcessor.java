package com.particle.game.item.use.place;

import com.particle.api.item.IItemPlaceProcessor;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.attribute.BlockAttributeService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.BlockFace;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemSignProcessor implements IItemPlaceProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ItemSignProcessor.class);

    protected EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    protected PlayerInventoryAPI playerInventoryService;

    @Inject
    protected InventoryManager inventoryManager;

    @Inject
    protected LevelService levelService;

    @Inject
    protected BlockService blockService;

    @Inject
    private BlockAttributeService blockAttributeService;

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        BlockPrototype blockAt = this.levelService.getBlockTypeAt(player.getLevel(), itemUseInventoryData.getPosition());
        Vector3 position = itemUseInventoryData.getPosition();
        // 获取手上物品
        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack handItem = this.playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());

        // 若目標方塊不可覆蓋的
        if (!this.blockAttributeService.isCanBeCover(blockAt)) {
            position = position.getSide(itemUseInventoryData.getFace());
        }

        // 若欲放置的方塊位置已有方塊且不可被取代
        BlockPrototype placeBlock = levelService.getBlockTypeAt(player.getLevel(), position);
        if (!this.blockAttributeService.isCanBeCover(placeBlock)) {
            return;
        }

        Block block;
        // 若是下面
        if (itemUseInventoryData.getFace() == BlockFace.DOWN) {
            return;
        }

        // 若是側面
        if (itemUseInventoryData.getFace() != BlockFace.UP) {
            block = getTargetWoodWallSign(handItem);
        } else {
            block = getTargetWoodStandingSign(handItem);
        }


        // 执行放置操作
        boolean state = this.blockService.placeBlockByPlayer(player, block, handItem, position, itemUseInventoryData.getPosition(), itemUseInventoryData.getClickPosition());

        if (state) {
            handItem.setCount(handItem.getCount() - 1);
            if (handItem.getCount() <= 0) {
                handItem = ItemStack.getItem(ItemPrototype.AIR, 0);
            }
            this.playerInventoryService.setItem(playerInventory, playerInventory.getItemInHandle(), handItem);
        } else {
            this.playerInventoryService.notifyPlayerSlotChanged(playerInventory, playerInventory.getItemInHandle());
        }
    }

    private Block getTargetWoodWallSign(ItemStack handItem) {
        if (handItem.getItemType() == ItemPrototype.SIGN) {
            return Block.getBlock(BlockPrototype.WALL_SIGN);
        } else if (handItem.getItemType() == ItemPrototype.SPRUCE_SIGN) {
            return Block.getBlock(BlockPrototype.SPRUCE_WALL_SIGN);
        } else if (handItem.getItemType() == ItemPrototype.BIRCH_SIGN) {
            return Block.getBlock(BlockPrototype.BIRCH_WALL_SIGN);
        } else if (handItem.getItemType() == ItemPrototype.JUNGLE_SIGN) {
            return Block.getBlock(BlockPrototype.JUNGLE_WALL_SIGN);
        } else if (handItem.getItemType() == ItemPrototype.ACACIA_SIGN) {
            return Block.getBlock(BlockPrototype.ACACIA_WALL_SIGN);
        } else if (handItem.getItemType() == ItemPrototype.DARKOAK_SIGN) {
            return Block.getBlock(BlockPrototype.DARKOAK_WALL_SIGN);
        }

        return Block.getBlock(BlockPrototype.WALL_SIGN);
    }

    private Block getTargetWoodStandingSign(ItemStack handItem) {
        if (handItem.getItemType() == ItemPrototype.SIGN) {
            return Block.getBlock(BlockPrototype.STANDING_SIGN);
        } else if (handItem.getItemType() == ItemPrototype.SPRUCE_SIGN) {
            return Block.getBlock(BlockPrototype.SPRUCE_STANDING_SIGN);
        } else if (handItem.getItemType() == ItemPrototype.BIRCH_SIGN) {
            return Block.getBlock(BlockPrototype.BIRCH_STANDING_SIGN);
        } else if (handItem.getItemType() == ItemPrototype.JUNGLE_SIGN) {
            return Block.getBlock(BlockPrototype.JUNGLE_STANDING_SIGN);
        } else if (handItem.getItemType() == ItemPrototype.ACACIA_SIGN) {
            return Block.getBlock(BlockPrototype.ACACIA_STANDING_SIGN);
        } else if (handItem.getItemType() == ItemPrototype.DARKOAK_SIGN) {
            return Block.getBlock(BlockPrototype.DARKOAK_STANDING_SIGN);
        }

        return Block.getBlock(BlockPrototype.STANDING_SIGN);
    }
}
