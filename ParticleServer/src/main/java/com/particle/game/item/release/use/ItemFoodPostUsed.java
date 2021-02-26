package com.particle.game.item.release.use;

import com.particle.api.item.IItemProcessor;
import com.particle.api.item.IItemReleaseProcessor;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attribute.satisfaction.HungerService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.player.service.EntityUsingItemModule;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemReleaseInventoryData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 食物吃
 */
@Singleton
public class ItemFoodPostUsed implements IItemReleaseProcessor, IItemProcessor {

    private static final ECSModuleHandler<EntityUsingItemModule> ENTITY_USING_ITEM_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityUsingItemModule.class);


    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private PlayerService playerService;

    @Inject
    private HungerService hungerService;

    @Override
    public void process(Player player, ItemReleaseInventoryData itemReleaseInventoryData, InventoryActionData[] inventoryActionData) {
        EntityUsingItemModule entityUsingItemModule = ENTITY_USING_ITEM_MODULE_ECS_MODULE_HANDLER.getModule(player);
        if (entityUsingItemModule == null) {
            return;
        }

        entityUsingItemModule.setInventory(null);
    }

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        EntityUsingItemModule entityUsingItemModule = ENTITY_USING_ITEM_MODULE_ECS_MODULE_HANDLER.getModule(player);
        if (entityUsingItemModule == null) {
            return;
        }

        Inventory inventory = entityUsingItemModule.getInventory();
        if (inventory != null) {
            // 只消耗手持的物品
            PlayerInventory playerInventory = (PlayerInventory) inventory;
            ItemStack food = this.playerInventoryAPI.getItem(playerInventory, playerInventory.getItemInHandle());

            // 校验物品
            if (food == null) {
                return;
            }

            // 增加饥饿度
            this.hungerService.eatFood(player, food);

            // 生存模式则消耗物品
            if (this.playerService.getGameMode(player) == GameMode.SURVIVE) {
                food.setCount(food.getCount() - 1);
                if (food.getCount() > 0) {
                    this.playerInventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), food);
                } else {
                    this.playerInventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), ItemStack.getItem(ItemPrototype.AIR));
                }
            }

            entityUsingItemModule.setInventory(null);
        } else {
            // 只有饥饿度不满的时候才允许吃
            if (this.hungerService.isFull(player)) {
                entityUsingItemModule.setInventory(this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER));
            } else {
                this.playerService.updatePlayerAttribute(player);
            }
        }
    }
}
