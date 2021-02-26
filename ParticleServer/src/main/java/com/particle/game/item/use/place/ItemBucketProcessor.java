package com.particle.game.item.use.place;

import com.particle.api.item.IItemPlaceProcessor;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.player.inventory.transaction.TransactionManager;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.geometry.BlockGeometry;
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
public class ItemBucketProcessor implements IItemPlaceProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemBucketProcessor.class);

    @Inject
    private PlayerInventoryAPI playerInventoryService;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private TransactionManager transactionManager;

    @Inject
    private LevelService levelService;

    @Inject
    private BlockService blockService;

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);

        // 以服务端数据为准
        ItemStack handItem = this.playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
        if (handItem == null || handItem.getItemType() != ItemPrototype.BUCKET) {
            return;
        }

        // 表示水桶 或 岩漿桶
        if (handItem.getMeta() == 8 || handItem.getMeta() == 10) {
            Block placedBlock;
            if (handItem.getMeta() == 8) {
                placedBlock = Block.getBlock(BlockPrototype.WATER);
            } else {
                placedBlock = Block.getBlock(BlockPrototype.LAVA);
            }

            // 目标方块位置判断
            BlockPrototype blockAt = this.levelService.getBlockTypeAt(player.getLevel(), itemUseInventoryData.getPosition());
            Vector3 position = itemUseInventoryData.getPosition();
            if (blockAt.getBlockGeometry() != BlockGeometry.EMPTY) {
                position = position.getSide(itemUseInventoryData.getFace());
            }

            boolean state = this.blockService.placeBlockByPlayer(player, placedBlock, handItem, position, itemUseInventoryData.getPosition(), itemUseInventoryData.getClickPosition());

            if (state) {
                ItemStack itemStack = playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
                itemStack.setMeta(0);
                playerInventoryService.setItem(playerInventory, playerInventory.getItemInHandle(), itemStack);
            } else {
                // 将客户端自动添加的空桶去掉
                playerInventoryService.notifyPlayerContentChanged(playerInventory);
            }
        }
        // 若是空桶
        else if (handItem.getMeta() == 0) {
            // 目标方块判断
            BlockPrototype blockAt = this.levelService.getBlockTypeAt(player.getLevel(), itemUseInventoryData.getPosition());

            // 若目標是水源
            if (blockAt == BlockPrototype.WATER) {
                handItem.setMeta(8);
            }
            // 若目標是岩漿源
            else if (blockAt == BlockPrototype.LAVA) {
                handItem.setMeta(10);
            }

            // 若不是對兩者使用
            if (handItem.getMeta() == 0) {
                return;
            }

            boolean state = this.blockService.placeBlockByPlayer(player, Block.getBlock(BlockPrototype.AIR), handItem, itemUseInventoryData.getPosition(), itemUseInventoryData.getPosition(), itemUseInventoryData.getClickPosition());
            if (state) {
                ItemStack itemStack = playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
                itemStack.setCount(itemStack.getCount() - 1);
                playerInventoryService.setItem(playerInventory, playerInventory.getItemInHandle(), itemStack);

                handItem.setCount(1);
                playerInventoryService.addItem(playerInventory, handItem);
            } else {
                playerInventoryService.notifyPlayerContentChanged(playerInventory);
            }
        }
    }
}
