package com.particle.game.entity.attack.processor;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.movement.module.AutoDirectionModule;
import com.particle.game.world.physical.EntityColliderService;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Consumer;

@Singleton
public class ArrowHitEntityProcessor implements IHitEntityProcessor {

    private static final ECSModuleHandler<AutoDirectionModule> AUTO_DIRECTION_MODULE_HANDLER = ECSModuleHandler.buildHandler(AutoDirectionModule.class);

    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;

    @Inject
    private EntityColliderService entityColliderService;

    @Override
    public Consumer<Entity> getColliderEntityCallback(Entity damager, ItemStack weapon, Entity projectile) {
        return new ColliderEntityCallback(damager, weapon, projectile);
    }

    @Override
    public Consumer<Vector3f> getColliderBlockCallback(Entity damager, ItemStack weapon, Entity projectile) {
        return new ColliderBlockCallback(damager, projectile);
    }

    private class ColliderBlockCallback implements Consumer<Vector3f> {
        private Entity damager;
        private Entity projectile;

        public ColliderBlockCallback(Entity damager, Entity projectile) {
            this.damager = damager;
            this.projectile = projectile;
        }

        @Override
        public void accept(Vector3f position) {
            entityColliderService.removeColliderEntityCallback(projectile);
            entityColliderService.removeCollider(projectile);

            AUTO_DIRECTION_MODULE_HANDLER.removeModule(projectile);
        }
    }

    private class ColliderEntityCallback implements Consumer<Entity> {

        private Entity damager;
        private ItemStack weapon;
        private Entity projectile;

        public ColliderEntityCallback(Entity damager, ItemStack weapon, Entity projectile) {
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
