package com.particle.game.item.use.place;

import com.particle.api.item.IItemPlaceProcessor;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.interactor.BlockWorldService;
import com.particle.game.item.DurabilityService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.events.level.block.BlockCultivatedEvent;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemHoeProcessor implements IItemPlaceProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ItemHoeProcessor.class);

    @Inject
    private LevelService levelService;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private DurabilityService durabilityService;

    @Inject
    private BlockWorldService blockWorldService;

    @Inject
    protected PlayerInventoryAPI playerInventoryService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        Vector3 vector3 = itemUseInventoryData.getPosition();
        BlockPrototype targetBlock = this.levelService.getBlockTypeAt(player.getLevel(), vector3);
        if (targetBlock == null) {
            return;
        }
        if (inventoryActionData.length != 0) {
            // 不采用客户端的消耗
            PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player,
                    InventoryConstants.CONTAINER_ID_PLAYER);
            playerInventoryService.notifyPlayerSlotChanged(playerInventory, playerInventory.getItemInHandle());
        }

        // 锄头用于泥土，草方块，草径方块上
        if (targetBlock != BlockPrototype.DIRT && targetBlock != BlockPrototype.GRASS &&
                targetBlock != BlockPrototype.GRASS_PATH) {
            return;
        }

        // 发送事件
        BlockCultivatedEvent blockCultivatedEvent = new BlockCultivatedEvent(player.getLevel());
        blockCultivatedEvent.setPlayer(player);
        blockCultivatedEvent.setBlock(this.levelService.getBlockAt(player.getLevel(), vector3));
        blockCultivatedEvent.setPosition(vector3);
        this.eventDispatcher.dispatchEvent(blockCultivatedEvent);

        // 判断事件是否被取消
        if (blockCultivatedEvent.isCancelled()) {
            return;
        }

        // 设置方块
        Block newBlock = Block.getBlock(BlockPrototype.FARMLAND);
        newBlock.setMeta(0);
        this.levelService.setBlockAt(player.getLevel(), newBlock, vector3);
        // 初始化对应的blockEntity
        this.blockWorldService.onBlockPlaced(player.getLevel(), player, newBlock, vector3);


        // 消耗耐久度
        this.durabilityService.consumptionItem(player);
    }
}
