package com.particle.game.entity.attack.processor;

import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.state.EntityStateService;
import com.particle.game.entity.state.model.EntityStateType;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Consumer;

@Singleton
public class SmallFireBallEntityProcessor implements IHitEntityProcessor {

    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private EntityStateService entityStateService;

    @Override
    public Consumer<Entity> getColliderEntityCallback(Entity damager, ItemStack weapon, Entity projectile) {
        return new ColliderEntityCallback(damager, weapon, projectile);
    }

    @Override
    public Consumer<Vector3f> getColliderBlockCallback(Entity damager, ItemStack weapon, Entity projectile) {
        return new ColliderBlockCallback(projectile);
    }

    private class ColliderBlockCallback implements Consumer<Vector3f> {
        private Entity projectile;

        public ColliderBlockCallback(Entity projectile) {
            this.projectile = projectile;
        }

        @Override
        public void accept(Vector3f position) {
            // TODO: 2019/4/17 等自动灭火功能ok后，设置起火

            entitySpawnService.despawn(projectile);
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

            entitySpawnService.despawn(projectile);

            // 计算与应用伤害
            entityRemoteAttackService.projectileHitEntity(damager, target, projectile, weapon);

            // 额外效果
            entityStateService.enableState(target, EntityStateType.ON_FIRE.getName(), EntityStateType.ON_FIRE.getDefaultUpdateInterval(), 5000);
        }
    }
}
