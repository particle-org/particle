package com.particle.game.item.use.place;

import com.particle.api.item.IItemPlaceProcessor;
import com.particle.game.block.planting.CocoaService;
import com.particle.game.block.planting.MushroomService;
import com.particle.game.item.use.place.planting.ItemCocoaPlaced;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemDyeProcessor implements IItemPlaceProcessor {

    @Inject
    private MushroomService mushroomService;

    @Inject
    private CocoaService cocoaService;

    @Inject
    private BlockService blockService;

    @Inject
    private LevelService levelService;

    @Inject
    private ItemCocoaPlaced itemCocoaPlaced;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private PlayerInventoryAPI playerInventoryService;

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        ItemStack item = itemUseInventoryData.getItem();
        Vector3 position = itemUseInventoryData.getPosition();
        Level level = player.getLevel();
        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        boolean state = false;

        if (this.mushroomService.isTargetDye(item) && this.mushroomService.isMushroom(level, position)) {
            level.getLevelSchedule().scheduleSimpleTask("mushroom_check_dye_and_grow",
                    () -> mushroomService.checkDyeAndGrow(level, item, position));
            state = true;
        } else if (this.cocoaService.isCocoaSeed(item)) {
            this.itemCocoaPlaced.process(player, itemUseInventoryData, inventoryActionData);
        } else if (getPlantBlock(level, position) != null) {
            // 若是植物
            state = this.blockService.blockGrowUp(level, position);
        }


        if (state) {
            ItemStack itemStack = playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
            itemStack.setCount(itemStack.getCount() - 1);
            if (itemStack.getCount() <= 0) {
                itemStack = ItemStack.getItem(ItemPrototype.AIR, 0);
            }
            playerInventoryService.setItem(playerInventory, playerInventory.getItemInHandle(), itemStack);
        } else {
            ItemStack itemStack = playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
            itemStack.setCount(itemStack.getCount());
            playerInventoryService.setItem(playerInventory, playerInventory.getItemInHandle(), itemStack);
        }
    }

    public BlockPrototype getPlantBlock(Level level, Vector3 position) {
        BlockPrototype block = levelService.getBlockTypeAt(level, position);
        if (block == BlockPrototype.WHEAT ||
                block == BlockPrototype.CARROTS ||
                block == BlockPrototype.POTATOES ||
                block == BlockPrototype.BEETROOT ||
                block == BlockPrototype.MELON_STEM ||
                block == BlockPrototype.PUMPKIN_STEM) {
            return block;
        }

        return null;
    }
}
