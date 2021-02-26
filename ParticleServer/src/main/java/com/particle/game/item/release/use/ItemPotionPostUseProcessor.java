package com.particle.game.item.release.use;

import com.particle.api.item.IItemProcessor;
import com.particle.api.item.IItemReleaseProcessor;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.state.EntityStateService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.player.service.EntityUsingItemModule;
import com.particle.model.effect.EffectBaseData;
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
import com.particle.model.potion.Potions;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemPotionPostUseProcessor implements IItemReleaseProcessor, IItemProcessor {

    private static final ECSModuleHandler<EntityUsingItemModule> ENTITY_USING_ITEM_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityUsingItemModule.class);


    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private EntityStateService entityStateService;

    @Inject
    private HealthServiceProxy healthServiceProxy;

    @Inject
    private PlayerService playerService;

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
            ItemStack potionItem = this.playerInventoryAPI.getItem(playerInventory, playerInventory.getItemInHandle());

            // 校验物品
            if (potionItem == null || potionItem.getItemType() != ItemPrototype.POTION) {
                return;
            }

            // 获取药水类型
            Potions potion = Potions.fromId(potionItem.getMeta());
            if (potion != null) {
                EffectBaseData effectBaseData = potion.getEffectBaseData();

                if (effectBaseData != null && effectBaseData.getEffectType() != null) {
                    this.entityStateService.enableState(player, effectBaseData.getEffectType().getName(), effectBaseData.getLevel(), -1, effectBaseData.getDuration() * 1000);

                    switch (potion.getType()) {
                        case HEALING:
                            this.healthServiceProxy.healing(player, 4 * effectBaseData.getLevel());

                            break;
                    }
                }
            }

            // 生存模式则消耗物品
            if (this.playerService.getGameMode(player) == GameMode.SURVIVE) {
                this.playerInventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), ItemStack.getItem(ItemPrototype.AIR));
            }

            // 计算返回的玻璃瓶
            int resultSlot = -1;
            for (InventoryActionData inventoryActionDatum : inventoryActionData) {
                if (inventoryActionDatum.getToItem().getItemType() == ItemPrototype.GLASS_BOTTLE) {
                    resultSlot = inventoryActionDatum.getSlot();
                }
            }
            // 发放玻璃瓶
            if (resultSlot == -1) {
                this.playerInventoryAPI.addItem(playerInventory, ItemStack.getItem(ItemPrototype.GLASS_BOTTLE));
            } else {
                ItemStack glassBottle = this.playerInventoryAPI.getItem(playerInventory, resultSlot);
                if (glassBottle.getItemType() == ItemPrototype.AIR) {
                    glassBottle = ItemStack.getItem(ItemPrototype.GLASS_BOTTLE);
                } else if (glassBottle.getItemType() == ItemPrototype.GLASS_BOTTLE) {
                    glassBottle.setCount(glassBottle.getCount() + 1);
                } else {
                    return;
                }

                this.playerInventoryAPI.setItem(playerInventory, resultSlot, glassBottle);
            }


            entityUsingItemModule.setInventory(null);
        } else {
            entityUsingItemModule.setInventory(this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER));
        }
    }
}
