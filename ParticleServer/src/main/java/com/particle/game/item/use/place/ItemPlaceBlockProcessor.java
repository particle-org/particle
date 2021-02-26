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
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemPlaceBlockProcessor implements IItemPlaceProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ItemPlaceBlockProcessor.class);

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

        // 若目標方塊不可覆蓋的 或是 手持睡蓮
        if (!this.blockAttributeService.isCanBeCover(blockAt) || handItem.getItemType() == ItemPrototype.WATERLILY) {
            position = position.getSide(itemUseInventoryData.getFace());
        }

        // 若欲放置的方塊位置已有方塊且不可被取代
        BlockPrototype placeBlock = levelService.getBlockTypeAt(player.getLevel(), position);
        if (!this.blockAttributeService.isCanBeCover(placeBlock)) {
            return;
        }

        Block placedBlock;
        placedBlock = getBlock(player, itemUseInventoryData);

        // 对于id小于256的，其block继承其物品的meta属性
        if (handItem.getItemType().getId() < 256) {
            placedBlock.setMeta(handItem.getMeta());
        }

        // 执行放置操作
        boolean state = this.blockService.placeBlockByPlayer(player, placedBlock, handItem, position, itemUseInventoryData.getPosition(), itemUseInventoryData.getClickPosition());

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

    /**
     * 获取放置的新方块
     *
     * @param player
     * @param itemUseInventoryData
     * @return
     */
    protected Block getBlock(Player player, ItemUseInventoryData itemUseInventoryData) {
        ItemStack handItem = itemUseInventoryData.getItem();
        if (handItem == null) {
            return null;
        }

        Block block;
        if (handItem.getItemType() == ItemPrototype.REDSTONE) {
            block = Block.getBlock(BlockPrototype.REDSTONE_WIRE);
        } else {
            block = Block.getBlock(handItem.getItemType().getName());
        }

        // 对于id小于256的，其block继承其物品的meta属性
        if (handItem.getItemType().getId() < 256) {
            block.setMeta(handItem.getMeta());
        }
        return block;
    }
}
