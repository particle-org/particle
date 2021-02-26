package com.particle.game.entity.attack.processor;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.movement.module.AutoDirectionModule;
import com.particle.game.entity.spawn.AutoRemovedModule;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.item.ItemBindModule;
import com.particle.game.item.PickableModule;
import com.particle.game.item.RecycleModule;
import com.particle.game.world.physical.EntityColliderService;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Consumer;

@Singleton
public class TridentHitEntityProcessor implements IHitEntityProcessor {

    private static final ECSModuleHandler<AutoDirectionModule> AUTO_DIRECTION_MODULE_HANDLER = ECSModuleHandler.buildHandler(AutoDirectionModule.class);

    private static final ECSModuleHandler<PickableModule> PICKABLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(PickableModule.class);

    private static final ECSModuleHandler<AutoRemovedModule> AUTO_REMOVED_MODULE_HANDLER = ECSModuleHandler.buildHandler(AutoRemovedModule.class);

    private static final ECSModuleHandler<ItemBindModule> ITEM_BIND_MODULE_HANDLER = ECSModuleHandler.buildHandler(ItemBindModule.class);

    private static final ECSModuleHandler<RecycleModule> RECYCLE_MODULE_HANDLER = ECSModuleHandler.buildHandler(RecycleModule.class);


    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;

    @Inject
    private EntityColliderService entityColliderService;

    @Override
    public Consumer<Entity> getColliderEntityCallback(Entity damager, ItemStack weapon, Entity projectile) {
        return new EntityColliderCallback(damager, weapon, projectile);
    }

    @Override
    public Consumer<Vector3f> getColliderBlockCallback(Entity damager, ItemStack weapon, Entity projectile) {
        return new BlockColliderCallback(damager, weapon, projectile);
    }

    private class BlockColliderCallback implements Consumer<Vector3f> {
        private Entity damager;
        private Entity projectile;
        private ItemStack weapon;

        public BlockColliderCallback(Entity damager, ItemStack weapon, Entity projectile) {
            this.damager = damager;
            this.weapon = weapon;
            this.projectile = projectile;
        }

        @Override
        public void accept(Vector3f position) {
            // 若有忠誠
            if (ItemAttributeService.hasEnchantment(weapon, Enchantments.LOYALTY)) {
                // 添加 component
                RecycleModule recycleModule = RECYCLE_MODULE_HANDLER.getModule(projectile);
                if (recycleModule == null) {
                    recycleModule = RECYCLE_MODULE_HANDLER.bindModule(projectile);
                    recycleModule.setEntity(damager);
                    recycleModule.setEnchantmentLevel(1);
                    recycleModule.setWeapon(weapon);
                }
            }
            // 否則可撿
            else {
                PickableModule pickableModule = PICKABLE_MODULE_HANDLER.bindModule(projectile);
                pickableModule.setBinderEntity(damager.getRuntimeId());

                AUTO_REMOVED_MODULE_HANDLER.bindModule(projectile);

                weapon.setCount(1);

                ItemBindModule itemBindModule = ITEM_BIND_MODULE_HANDLER.bindModule(projectile);
                itemBindModule.setItem(weapon);
            }

            entityColliderService.removeColliderEntityCallback(projectile);
            entityColliderService.removeCollider(projectile);

            AUTO_DIRECTION_MODULE_HANDLER.removeModule(projectile);
        }
    }

    private class EntityColliderCallback implements Consumer<Entity> {
        private Entity damager;
        private Entity projectile;
        private ItemStack weapon;

        public EntityColliderCallback(Entity damager, ItemStack weapon, Entity projectile) {
            this.damager = damager;
            this.weapon = weapon;
            this.projectile = projectile;
        }

        @Override
        public void accept(Entity target) {
            if (target == damager) {
                return;
            }

            entityRemoteAttackService.projectileHitEntity(damager, target, projectile, weapon);
        }

    }
}
