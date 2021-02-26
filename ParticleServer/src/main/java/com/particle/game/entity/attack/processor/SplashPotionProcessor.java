package com.particle.game.entity.attack.processor;

import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.state.EntityStateService;
import com.particle.game.player.PlayerService;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.effect.EffectBaseData;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.item.ItemStack;
import com.particle.model.level.LevelEventType;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import com.particle.model.potion.Potions;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.function.Consumer;

@Singleton
public class SplashPotionProcessor implements IHitEntityProcessor {

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private PositionService positionService;

    @Inject
    private PlayerService playerService;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private EntityStateService entityStateService;

    @Inject
    private ParticleService particleService;


    @Override
    public Consumer<Entity> getColliderEntityCallback(Entity damager, ItemStack weapon, Entity projectile) {
        return new ColliderEntityCallback(projectile, damager);
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
            handle(this.projectile, position);
        }
    }

    private class ColliderEntityCallback implements Consumer<Entity> {
        private Entity projectile;
        private Entity damager;

        public ColliderEntityCallback(Entity projectile, Entity damager) {
            this.projectile = projectile;
            this.damager = damager;
        }

        @Override
        public void accept(Entity target) {
            if (target == damager) {
                return;
            }

            handle(this.projectile, positionService.getPosition(target));
        }
    }

    private void handle(Entity projectile, Vector3f position) {
        // 移除抛射物
        entitySpawnService.despawn(projectile);

        // 设置buff
        List<Player> nearPlayers = playerService.getNearPlayers(projectile.getLevel(), position, 3);

        Potions potions = Potions.fromId(metaDataService.getShortData(projectile, EntityMetadataType.POTION_AUX_VALUE));
        for (Player nearPlayer : nearPlayers) {
            EffectBaseData effectBaseData = potions.getEffectBaseData();
            this.entityStateService.enableState(nearPlayer, effectBaseData.getEffectType().getName(), effectBaseData.getLevel(), -1, effectBaseData.getDuration() * 1000);
        }

        // 播方粒子效果
        particleService.playParticle(projectile.getLevel(), LevelEventType.ParticlesPotionSplash, Block.getBlock(BlockPrototype.AIR), position, potions.getEffectBaseData().getEffectType().getColor());
    }
}
