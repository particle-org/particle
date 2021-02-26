package com.particle.game.entity.attack.processor;

import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.MobEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.EntityType;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.events.level.entity.EggSpawnChickenEvent;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;
import java.util.function.Consumer;

@Singleton
public class EggEntityProcessor implements IHitEntityProcessor {

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private MobEntityService mobEntityService;

    @Inject
    private Random random;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private PositionService positionService;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Override
    public Consumer<Entity> getColliderEntityCallback(Entity damager, ItemStack weapon, Entity projectile) {
        return new ColliderEntityCallback(damager, weapon, projectile);
    }

    @Override
    public Consumer<Vector3f> getColliderBlockCallback(Entity damager, ItemStack weapon, Entity projectile) {
        return new ColliderBlockCallback(damager, weapon, projectile);
    }

    private class ColliderBlockCallback implements Consumer<Vector3f> {
        private Entity damager;
        private ItemStack weapon;
        private Entity projectile;

        public ColliderBlockCallback(Entity damager, ItemStack weapon, Entity projectile) {
            this.damager = damager;
            this.weapon = weapon;
            this.projectile = projectile;
        }

        @Override
        public void accept(Vector3f position) {
            handle(this.damager, this.projectile, position);
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

            handle(this.damager, this.projectile, positionService.getPosition(target));
        }

    }

    private void handle(Entity damager, Entity projectile, Vector3f position) {
        entitySpawnService.despawn(projectile);

        int rand = random.nextInt(32);
        if (rand == 0) {
            // 發送雞蛋生雞事件
            EggSpawnChickenEvent eggSpawnChickenEvent = new EggSpawnChickenEvent(damager.getLevel());
            eggSpawnChickenEvent.setEntity(damager);
            eggSpawnChickenEvent.setPosition(position);
            this.eventDispatcher.dispatchEvent(eggSpawnChickenEvent);
            if (eggSpawnChickenEvent.isCancelled()) {
                return;
            }


            MobEntity targetEntity = mobEntityService.createEntity(EntityType.CHICKEN.type(), position.add(0, 0.2f, 0));
            metaDataService.setFloatData(targetEntity, EntityMetadataType.SCALE, metaDataService.getFloatData(targetEntity, EntityMetadataType.SCALE), false);

            boolean state = entitySpawnService.spawnEntity(damager.getLevel(), targetEntity);
            // 配置AI
            if (state) {
                targetEntity.getLevel().getLevelSchedule().scheduleSimpleTask("UpdateAI", () -> {
                    entityDecisionServiceProxy.updateResponse(targetEntity, targetEntity.getActorType());
                    entityDecisionServiceProxy.updateDecision(targetEntity, targetEntity.getActorType());
                });
            }
        }
    }
}
