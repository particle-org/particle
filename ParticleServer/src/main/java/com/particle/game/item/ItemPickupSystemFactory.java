package com.particle.game.item;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.ItemEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.BeforeItemPickupEvent;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ItemPickupSystemFactory implements ECSSystemFactory<ItemPickupSystemFactory.ItemPickupSystem> {

    private static final ECSModuleHandler<PickableModule> PICKABLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(PickableModule.class);
    private static final ECSModuleHandler<ItemBindModule> ITEM_BIND_MODULE_HANDLER = ECSModuleHandler.buildHandler(ItemBindModule.class);

    @Inject
    private PlayerService playerService;
    @Inject
    private ItemEntityService itemEntityService;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private PositionService positionService;

    @Inject
    private InventoryManager inventoryManager;
    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    public class ItemPickupSystem implements ECSSystem {

        private Entity entity;
        private PickableModule pickableModule;
        private ItemBindModule itemBindModule;

        public ItemPickupSystem(Entity entity) {
            this.entity = entity;

            this.pickableModule = PICKABLE_MODULE_HANDLER.getModule(entity);
            this.itemBindModule = ITEM_BIND_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            if (this.pickableModule.getPickupCooling() > 0) {
                this.pickableModule.cooldown();

                return;
            }

            // 设置冷却，避免频繁判断影响效率
            pickableModule.setPickupCooling(2);

            // 查找最近可以捡拾物品的玩家
            Player player = playerService.getClosestPlayer(entity.getLevel(), positionService.getPosition(entity), 1.5f);

            if (player != null) {
                // 出于效率考虑，就在里面做判断
                if (pickableModule.getBinderEntity() != -1 && pickableModule.getBinderEntity() != player.getRuntimeId()) {
                    return;
                }

                // 移除物品
                ItemStack itemStack = this.itemBindModule.getItem();
                if (itemStack != null) {
                    BeforeItemPickupEvent beforeItemPickupEvent = new BeforeItemPickupEvent(player, player.getLevel(), entity, itemStack.getItemType());
                    EventDispatcher.getInstance().dispatchEvent(beforeItemPickupEvent);
                    if (beforeItemPickupEvent.isCancelled()) {
                        return;
                    }
                }

                BeforeItemPickupEvent beforeItemPickupEvent = new BeforeItemPickupEvent(player, player.getLevel(), entity, itemStack.getItemType());
                EventDispatcher.getInstance().dispatchEvent(beforeItemPickupEvent);
                if (beforeItemPickupEvent.isCancelled()) {
                    return;
                }

                PlayerInventory playerInventory = (PlayerInventory) inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
                List<ItemStack> itemStacks = playerInventoryAPI.addItem(playerInventory, itemStack);
                if (itemStacks.size() != 0) {
                    // 拣成功
                    if (itemStacks.get(0).getCount() != itemStack.getCount()) {
                        // 播放动画
                        if (entity instanceof ItemEntity) {
                            itemEntityService.pickAnimate(player, (ItemEntity) entity);
                        }

                        itemStack.setCount(itemStacks.get(0).getCount());
                    }
                } else {
                    // 播放动画
                    if (entity instanceof ItemEntity) {
                        itemEntityService.pickAnimate(player, (ItemEntity) entity);
                    }

                    entitySpawnService.despawn(entity);
                }
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{PickableModule.class, ItemBindModule.class};
    }

    @Override
    public ItemPickupSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new ItemPickupSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<ItemPickupSystem> getSystemClass() {
        return ItemPickupSystem.class;
    }
}
