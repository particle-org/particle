package com.particle.game.entity.systems;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.entity.attribute.health.EntityHealthModule;
import com.particle.game.entity.attribute.satisfaction.EntitySatisfactionModule;
import com.particle.game.entity.state.EntityStateModule;
import com.particle.game.entity.state.model.EntityStateType;
import com.particle.model.entity.Entity;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.type.EntityStateRecorder;
import com.particle.model.events.level.entity.EntityDamageType;
import com.particle.model.network.packets.data.UpdateAttributesPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HealthSystemFactory implements ECSSystemFactory<HealthSystemFactory.HealthSystem> {
    @Inject
    private NetworkManager networkManager;

    private static final ECSModuleHandler<EntityHealthModule> ENTITY_HEALTH_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityHealthModule.class);
    private static final ECSModuleHandler<EntitySatisfactionModule> ENTITY_SATISFACTION_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntitySatisfactionModule.class);
    private static final ECSModuleHandler<EntityStateModule> ENTITY_STATE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityStateModule.class);

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{EntityHealthModule.class, EntitySatisfactionModule.class, EntityStateModule.class};
    }

    @Override
    public HealthSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new HealthSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<HealthSystem> getSystemClass() {
        return HealthSystem.class;
    }

    public class HealthSystem implements ECSSystem {

        private Entity entity;
        private EntityHealthModule healthModule;
        private EntityStateModule entityStateModule;
        private EntitySatisfactionModule satisfactionModule;

        HealthSystem(Entity entity) {
            this.entity = entity;
            this.healthModule = ENTITY_HEALTH_MODULE_HANDLER.getModule(entity);
            this.entityStateModule = ENTITY_STATE_MODULE_HANDLER.getModule(entity);
            this.satisfactionModule = ENTITY_SATISFACTION_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            if (!this.healthModule.requestUpdate()) {
                return;
            }

            if (!this.healthModule.isAlive()) {
                return;
            }

            // 饥饿度生命处理
            int foodLevel = this.satisfactionModule.getFoodLevel();
            if (foodLevel > 16) {
                if (this.healthModule.getHealth() < this.healthModule.getMaxHealth()) {
                    this.healthModule.healing(0.5f);
                    this.satisfactionModule.addFoodLevel(-1);
                    this.satisfactionModule.addFoodExhaustionLevel(1f);

                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                        updateAttributesPacket.setEntityId(player.getRuntimeId());
                        updateAttributesPacket.setAttributes(new EntityAttribute[]{satisfactionModule.getEntityHungerAttribute(), satisfactionModule.getEntitySaturationAttribute()});
                        networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
                    }
                }
            } else if (foodLevel == 0) {
                if (this.healthModule.getHealth() > 10) {
                    this.healthModule.applyDamage(0.5f);
                }
            }

            // 药水处理
            if (entityStateModule.hasState(EntityStateType.REGENERATION)) {
                EntityStateRecorder state = entityStateModule.getState(EntityStateType.REGENERATION.getName());

                this.healthModule.healing(1f * state.getLevel());
            }

            if (entityStateModule.hasState(EntityStateType.POISON)) {
                EntityStateRecorder state = entityStateModule.getState(EntityStateType.POISON.getName());

                float damage = 2f * state.getLevel();
                float health = this.healthModule.getHealth();
                if (health > damage) {
                    this.healthModule.damageEntity(damage, EntityDamageType.Wither, null);
                } else if (health > 1) {
                    this.healthModule.damageEntity(health - 1, EntityDamageType.Wither, null);
                }
            }

            if (entityStateModule.hasState(EntityStateType.WITHER)) {
                EntityStateRecorder state = entityStateModule.getState(EntityStateType.WITHER.getName());

                float damage = 1f * state.getLevel();
                float health = this.healthModule.getHealth();
                if (health > damage) {
                    this.healthModule.applyDamage(damage);
                } else if (health > 1) {
                    this.healthModule.applyDamage(health - 1);
                }
            }
        }
    }
}
