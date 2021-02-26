package com.particle.game.item.use.place;

import com.particle.api.item.IItemPlaceProcessor;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.Player;

import javax.inject.Inject;

public class GlassBottleProcessor implements IItemPlaceProcessor {

    @Inject
    private PlayerInventoryAPI playerInventoryService;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private LevelService levelService;

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);

        // 以服务端数据为准
        ItemStack handItem = this.playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
        if (handItem == null || handItem.getItemType() != ItemPrototype.GLASS_BOTTLE) {
            return;
        }

        if (handItem.getMeta() == 0) {
            // 目标方块判断
            BlockPrototype blockAt = this.levelService.getBlockTypeAt(player.getLevel(), itemUseInventoryData.getPosition());

            // 若目標是水源
            if (blockAt == BlockPrototype.WATER || blockAt == BlockPrototype.FLOWING_WATER) {
                if (handItem.getCount() == 1) {
                    this.playerInventoryService.setItem(playerInventory, playerInventory.getItemInHandle(), ItemStack.getItem(ItemPrototype.POTION, 1), true);
                } else {
                    handItem.setCount(handItem.getCount() - 1);
                    this.playerInventoryService.setItem(playerInventory, playerInventory.getItemInHandle(), handItem);
                    this.playerInventoryService.addItem(playerInventory, ItemStack.getItem(ItemPrototype.POTION, 1));
                }

            }
        }
    }
}
