package com.particle.game.item;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.ItemEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ItemRecycleSystemFactory implements ECSSystemFactory<ItemRecycleSystemFactory.ItemRecycleSystem> {

    private static final ECSModuleHandler<RecycleModule> RECYCLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(RecycleModule.class);

    @Inject
    private PositionService positionService;

    @Inject
    private InventoryManager inventoryManager;
    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Inject
    private ItemEntityService itemEntityService;

    @Inject
    private EntitySpawnService entitySpawnService;

    public class ItemRecycleSystem implements ECSSystem {
        private Entity entity;
        private RecycleModule recycleModule;

        public ItemRecycleSystem(Entity entity) {
            this.entity = entity;
            this.recycleModule = RECYCLE_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            if (recycleModule != null) {
                // 若主人還在
                Entity master = this.recycleModule.getEntity();
                if (master != null) {
                    int returnTime = 5;
                    if (this.recycleModule.getRecycleTick() > returnTime) {
                        Vector3f targetPosition = positionService.getPosition(master);
                        Vector3f currentPosition = positionService.getPosition(entity);
                        Vector3f moveDistance = targetPosition.subtract(currentPosition);
                        // 微調位置
                        moveDistance.setY(moveDistance.getY() + 1);

                        positionService.setPosition(entity, targetPosition);
                        positionService.setDirection(entity, new Direction(moveDistance));

                        if (this.recycleModule.getRecycleTick() > 20 / this.recycleModule.getEnchantmentLevel()) {
                            ItemStack itemStack = this.recycleModule.getWeapon();
                            itemStack.setCount(1);
                            PlayerInventory playerInventory = (PlayerInventory) inventoryManager.getInventoryByContainerId(master, InventoryConstants.CONTAINER_ID_PLAYER);
                            List<ItemStack> itemStacks = playerInventoryAPI.addItem(playerInventory, itemStack);
                            if (itemStacks.size() != 0) {
                                // 拣成功
                                if (itemStacks.get(0).getCount() != itemStack.getCount()) {
                                    // 播放动画
                                    if (entity instanceof ItemEntity) {
                                        itemEntityService.pickAnimate((Player) master, (ItemEntity) entity);
                                    }

                                    itemStack.setCount(itemStacks.get(0).getCount());
                                }
                            } else {
                                // 播放动画
                                if (entity instanceof ItemEntity) {
                                    itemEntityService.pickAnimate((Player) master, (ItemEntity) entity);
                                }

                                entitySpawnService.despawn(entity);
                                return;
                            }
                        }
                    }

                    this.recycleModule.setRecycleTick(this.recycleModule.getRecycleTick() + 1);
                }
            }
        }
    }


    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{RecycleModule.class};
    }

    @Override
    public ItemRecycleSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new ItemRecycleSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<ItemRecycleSystem> getSystemClass() {
        return ItemRecycleSystem.class;
    }
}
