package com.particle.game.entity.attack.processor;

import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.player.PlayerService;
import com.particle.game.sound.SoundService;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.level.LevelEventType;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundId;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Consumer;

@Singleton
public class EnderPearHitProcessor implements IHitEntityProcessor {

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private PositionService positionService;

    @Inject
    private PlayerService playerService;

    @Inject
    private ParticleService particleService;

    @Inject
    private SoundService soundService;


    @Override
    public Consumer<Entity> getColliderEntityCallback(Entity damager, ItemStack weapon, Entity projectile) {
        return new ColliderEntityCallback(projectile, damager);
    }

    @Override
    public Consumer<Vector3f> getColliderBlockCallback(Entity damager, ItemStack weapon, Entity projectile) {
        return new ColliderBlockCallback(projectile, damager);
    }

    private class ColliderBlockCallback implements Consumer<Vector3f> {
        private Entity projectile;
        private Entity damager;

        public ColliderBlockCallback(Entity projectile, Entity damager) {
            this.projectile = projectile;
            this.damager = damager;
        }

        @Override
        public void accept(Vector3f position) {
            handle(damager, projectile, position);

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

            handle(damager, projectile, positionService.getPosition(target));
        }
    }

    private void handle(Entity damager, Entity projectile, Vector3f position) {
        if (!(damager instanceof Player)) {
            return;
        }

        Player player = (Player) damager;

        // 移除抛射物
        entitySpawnService.despawn(projectile);

        // 播方粒子效果
        particleService.playParticle(projectile.getLevel(), LevelEventType.ParticlesTeleport, position);
        soundService.broadcastLevelSound(projectile.getLevel(), position, SoundId.Teleport);

        // TP
        playerService.teleport(player, position);

    }
}
