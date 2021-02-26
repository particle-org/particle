package com.particle.game.entity.attack;

import com.particle.api.attack.IEntityAttackedHandle;
import com.particle.api.entity.EntityAttackedHandleServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attack.component.EntityAttackModule;
import com.particle.game.entity.attack.component.EntityAttackedModule;
import com.particle.game.server.Server;
import com.particle.game.world.animation.EntityAnimationService;
import com.particle.model.entity.Entity;
import com.particle.model.network.packets.data.EntityEventPacket;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityAttackedHandleService implements EntityAttackedHandleServiceApi {

    private static final ECSModuleHandler<EntityAttackedModule> ENTITY_ATTACKED_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityAttackedModule.class);
    private static final ECSModuleHandler<EntityAttackModule> ENTITY_ATTACK_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityAttackModule.class);


    private static final long ATTACK_RECORD_VALIDITY_PERIOD = 30000;

    @Inject
    private EntityAttackService entityAttackService;

    @Inject
    private EntityAnimationService entityAnimationService;

    @Inject
    private Server server;

    /**
     * 初始化生物被攻击的组件
     *
     * @param entity
     */
    public void initEntityAttackedComponent(Entity entity) {
        EntityAttackedModule entityAttackedModule = ENTITY_ATTACKED_MODULE_HANDLER.bindModule(entity);
        entityAttackedModule.setEntityAttackedHandle((interactor) -> {
            EntityAttackModule entityAttackModule = ENTITY_ATTACK_MODULE_HANDLER.getModule(interactor);
            if (entityAttackModule != null && entityAttackModule.canAttack()) {
                entityAnimationService.sendAnimation(interactor, EntityEventPacket.START_ATTACKING);
                this.entityAttackService.entityCloseAttack(interactor, entity);
            }
        });
    }

    /**
     * 初始化生物被攻击的组件
     *
     * @param entity
     * @param entityAttackedHandle
     */
    public void initEntityAttackedComponent(Entity entity, IEntityAttackedHandle entityAttackedHandle, boolean appendMode) {
        EntityAttackedModule entityAttackedModule = ENTITY_ATTACKED_MODULE_HANDLER.bindModule(entity);
        if (appendMode) {
            entityAttackedModule.setEntityAttackedHandle((interactor) -> {
                EntityAttackModule entityAttackModule = ENTITY_ATTACK_MODULE_HANDLER.getModule(interactor);
                if (entityAttackModule != null && entityAttackModule.canAttack()) {
                    entityAnimationService.sendAnimation(interactor, EntityEventPacket.START_ATTACKING);

                    boolean isSuccess = this.entityAttackService.entityCloseAttack(interactor, entity);
                    // 当entity成功被伤害之后才做相应的attacked动作
                    if (isSuccess) {
                        entityAttackedHandle.handle(interactor);
                    }
                }
            });
        } else {
            entityAttackedModule.setEntityAttackedHandle(entityAttackedHandle);
        }

    }

    /**
     * 攻击业务处理
     *
     * @param source
     * @param victim
     */
    public void entityAttackedByEntity(Entity source, Entity victim) {
        EntityAttackedModule entityAttackedModule = ENTITY_ATTACKED_MODULE_HANDLER.getModule(victim);
        if (entityAttackedModule != null) {
            // 如果是玩家，则记录伤害信息
            if (source instanceof Player) {
                Player player = (Player) source;
                entityAttackedModule.setLastDamager(player.getRuntimeId());
                entityAttackedModule.setLastdamageTimestamp(System.currentTimeMillis());
            }

            IEntityAttackedHandle entityAttackedHandle = entityAttackedModule.getEntityAttackedHandle();
            if (entityAttackedHandle != null) {
                entityAttackedHandle.handle(source);
            }
        }
    }

    /**
     * 查询生物的攻击者
     *
     * @param entity
     * @return
     */
    @Override
    public Player getEntityAttacker(Entity entity) {
        EntityAttackedModule entityAttackedModule = ENTITY_ATTACKED_MODULE_HANDLER.bindModule(entity);
        entityAttackedModule.setEntityAttackedHandle((interactor) -> {
            EntityAttackModule entityAttackModule = ENTITY_ATTACK_MODULE_HANDLER.getModule(interactor);
            if (entityAttackModule != null && entityAttackModule.canAttack()) {
                entityAnimationService.sendAnimation(interactor, EntityEventPacket.START_ATTACKING);
                this.entityAttackService.entityCloseAttack(interactor, entity);
            }
        });


        if (entityAttackedModule != null) {
            if (System.currentTimeMillis() - entityAttackedModule.getLastdamageTimestamp() < ATTACK_RECORD_VALIDITY_PERIOD) {
                return this.server.getPlayer(entityAttackedModule.getLastDamager());
            }
        }

        return null;
    }
}
