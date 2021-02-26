package com.particle.game.entity.attack;

import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.EntityService;
import com.particle.game.player.PlayerService;
import com.particle.game.sound.SoundService;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.events.level.entity.EntityDamageType;
import com.particle.model.events.level.entity.EntityExplosionDamageByEntityEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.CustomParticleType;
import com.particle.model.particle.ParticleType;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundId;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ExplosionService {

    @Inject
    private ParticleService particleService;

    @Inject
    private SoundService soundService;

    @Inject
    private EntityKnockBackService entityKnockBackService;

    @Inject
    private PositionService positionService;

    @Inject
    private PlayerService playerService;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private EntityService entityService;

    @Inject
    private HealthServiceProxy healthServiceProxy;

    public void explosionAt(Level level, Entity entity, Vector3f position, float power) {
        // Step 0 : 播方爆炸效果（上面的用于普通爆炸，下面的用于驽）
        this.particleService.playParticle(level, CustomParticleType.HUGE_EXPLOSION_EMITTER, position.add(0, 1, 0));
        this.particleService.playParticle(level, ParticleType.TYPE_HUGE_EXPLODE_SEED, position.add(0, 1, 0));

        this.soundService.broadcastLevelSound(level, position, SoundId.Explode);

        // Step 1 : 计算击退和伤害
        List<Player> nearPlayers = this.playerService.getNearPlayers(level, position, power);
        List<MobEntity> nearMobEntities = this.entityService.getNearMobEntities(level, position, power);
        List<MonsterEntity> nearMonsterEntities = this.entityService.getNearMonsterEntities(level, position, power);

        // 玩家
        for (Player nearPlayer : nearPlayers) {
            Vector3f knockback = this.entityKnockBackService.getExplosionKnockback(position, this.positionService.getPosition(nearPlayer), 5);

            if (knockback != null) {
                float damage = knockback.lengthSquared() / 100;

                // 爆炸事件
                EntityExplosionDamageByEntityEvent entityExplosionDamageByEntityEvent = new EntityExplosionDamageByEntityEvent(nearPlayer);
                entityExplosionDamageByEntityEvent.setDamage(damage);
                entityExplosionDamageByEntityEvent.setKnockback(knockback);
                entityExplosionDamageByEntityEvent.setDamager(entity);
                this.eventDispatcher.dispatchEvent(entityExplosionDamageByEntityEvent);

                // 若沒取消
                if (!entityExplosionDamageByEntityEvent.isCancelled()) {
                    this.healthServiceProxy.damageEntity(nearPlayer, damage, EntityDamageType.EntityExplosion, entity);
                    this.movementServiceProxy.setMotion(nearPlayer, knockback);
                }
            }
        }

        // 生物
        for (MobEntity mobEntity : nearMobEntities) {
            Vector3f knockback = this.entityKnockBackService.getExplosionKnockback(position, this.positionService.getPosition(mobEntity), 5);

            if (knockback != null) {
                float damage = knockback.lengthSquared() / 100;

                // 爆炸事件
                EntityExplosionDamageByEntityEvent entityExplosionDamageByEntityEvent = new EntityExplosionDamageByEntityEvent(mobEntity);
                entityExplosionDamageByEntityEvent.setDamage(damage);
                entityExplosionDamageByEntityEvent.setKnockback(knockback);
                entityExplosionDamageByEntityEvent.setDamager(entity);
                this.eventDispatcher.dispatchEvent(entityExplosionDamageByEntityEvent);

                // 若沒取消
                if (!entityExplosionDamageByEntityEvent.isCancelled()) {
                    this.healthServiceProxy.damageEntity(mobEntity, damage, EntityDamageType.EntityExplosion, entity);
                    this.movementServiceProxy.setMotion(mobEntity, knockback);
                }
            }
        }

        // Monster
        for (MonsterEntity monsterEntity : nearMonsterEntities) {
            Vector3f knockback = this.entityKnockBackService.getExplosionKnockback(position, this.positionService.getPosition(monsterEntity), 5);

            if (knockback != null) {
                float damage = knockback.lengthSquared() / 100;

                // 爆炸事件
                EntityExplosionDamageByEntityEvent entityExplosionDamageByEntityEvent = new EntityExplosionDamageByEntityEvent(monsterEntity);
                entityExplosionDamageByEntityEvent.setDamage(damage);
                entityExplosionDamageByEntityEvent.setKnockback(knockback);
                entityExplosionDamageByEntityEvent.setDamager(entity);
                this.eventDispatcher.dispatchEvent(entityExplosionDamageByEntityEvent);

                // 若沒取消
                if (!entityExplosionDamageByEntityEvent.isCancelled()) {
                    this.healthServiceProxy.damageEntity(monsterEntity, damage, EntityDamageType.EntityExplosion, entity);
                    this.movementServiceProxy.setMotion(monsterEntity, knockback);
                }
            }
        }
    }

}
