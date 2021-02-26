package com.particle.game.entity.attribute.health;

import com.particle.api.inject.RequestStaticInject;
import com.particle.core.aoi.SceneManager;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.attribute.EntityAttributeDataComponent;
import com.particle.game.entity.service.MobEntityService;
import com.particle.game.entity.service.MonsterEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.scene.module.BroadcastModule;
import com.particle.game.world.animation.EntityAnimationService;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.entity.Entity;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.attribute.EntityAttributeType;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.events.level.entity.EntityDamageType;
import com.particle.model.events.level.entity.EntityDamagedEvent;
import com.particle.model.events.level.entity.EntityDeathEvent;
import com.particle.model.network.packets.data.EntityEventPacket;
import com.particle.model.network.packets.data.UpdateAttributesPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import java.util.Map;

@RequestStaticInject
public class EntityHealthModule extends ECSModule {

    private static final ECSComponentHandler<EntityAttributeDataComponent> ENTITY_ATTRIBUTE_DATA_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntityAttributeDataComponent.class);
    private static final ECSComponentHandler<EntityHealthComponent> ENTITY_HEALTH_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntityHealthComponent.class);
    private static final ECSComponentHandler<EntityHealthAutoUpdateComponent> ENTITY_AUTO_HEALING_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntityHealthAutoUpdateComponent.class);

    private static final ECSModuleHandler<BroadcastModule> BROADCAST_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(BroadcastModule.class);

    @Inject
    private static EntityAnimationService entityAnimationService;
    @Inject
    private static MobEntityService mobEntityService;
    @Inject
    private static MonsterEntityService monsterEntityService;
    @Inject
    private static EntitySpawnService entitySpawnService;
    @Inject
    private static NetworkManager networkManager;
    @Inject
    private static BroadcastServiceProxy broadcastServiceProxy;

    private GameObject own;

    private static final Class[] REQUIRE_CLASSES = new Class[]{EntityAttributeDataComponent.class, EntityHealthComponent.class, EntityHealthAutoUpdateComponent.class};


    private EntityAttributeDataComponent entityAttributeDataComponent;
    private EntityHealthComponent entityHealthComponent;
    private EntityHealthAutoUpdateComponent entityHealthAutoUpdateComponent;

    // ----- 属性设置 -----
    public float getHealth() {
        return this.entityHealthComponent.getHealth();
    }

    public float getMaxHealth() {
        return this.entityHealthComponent.getMaxHealth();
    }

    public void setHealth(float health) {
        this.entityHealthComponent.setHealth(health);

        refreshEntityHealthAttribute();

        if (this.own instanceof Player) {
            Player player = (Player) this.own;

            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            updateAttributesPacket.setEntityId(player.getRuntimeId());
            updateAttributesPacket.setAttributes(new EntityAttribute[]{this.getEntityHealthAttribute(), this.getEntityAbsorptionAttribute()});

            networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
        }
    }

    public void setMaxHealth(float health) {
        this.entityHealthComponent.setMaxHealth(health);

        refreshEntityHealthAttribute();

        if (this.own instanceof Player) {
            Player player = (Player) this.own;

            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            updateAttributesPacket.setEntityId(player.getRuntimeId());
            updateAttributesPacket.setAttributes(new EntityAttribute[]{this.getEntityHealthAttribute(), this.getEntityAbsorptionAttribute()});

            networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
        }
    }

    public float getAbsorption() {
        return this.entityHealthComponent.getAbsorption();
    }

    public float getMaxAbsorption() {
        return this.entityHealthComponent.getMaxAbsorption();
    }

    public void setAbsorption(float absorption) {
        this.entityHealthComponent.setAbsorption(absorption);

        refreshEntityAbsorptionAttribute();

        if (this.own instanceof Player) {
            Player player = (Player) this.own;

            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            updateAttributesPacket.setEntityId(player.getRuntimeId());
            updateAttributesPacket.setAttributes(new EntityAttribute[]{this.getEntityHealthAttribute(), this.getEntityAbsorptionAttribute()});

            networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
        }
    }

    public void setMaxAbsorption(float maxAbsorption) {
        this.entityHealthComponent.setMaxAbsorption(maxAbsorption);

        refreshEntityAbsorptionAttribute();

        if (this.own instanceof Player) {
            Player player = (Player) this.own;

            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            updateAttributesPacket.setEntityId(player.getRuntimeId());
            updateAttributesPacket.setAttributes(new EntityAttribute[]{this.getEntityHealthAttribute(), this.getEntityAbsorptionAttribute()});

            networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
        }
    }

    // ----- 状态获取 -----
    public boolean isAlive() {
        return !this.entityHealthComponent.isDead();
    }

    public void updateMinDamageInterval(long minDamageInterval) {
        this.entityHealthComponent.setMinDamageInterval(minDamageInterval);
    }

    /**
     * 是否可以被伤害
     *
     * @return
     */
    public boolean canDamaged() {
        return System.currentTimeMillis() - this.entityHealthComponent.getLastDamageTime() > this.entityHealthComponent.getMinDamageInterval();
    }

    /**
     * 是否可以更新
     *
     * @return
     */
    public boolean requestUpdate() {
        boolean canUpdate = System.currentTimeMillis() - this.entityHealthAutoUpdateComponent.getLastHealingTime() > this.entityHealthAutoUpdateComponent.getMinDamageInterval();
        if (canUpdate) {
            this.entityHealthAutoUpdateComponent.setLastHealingTime(System.currentTimeMillis());
        }

        return canUpdate;
    }

    // ----- 生命操作 -----

    /**
     * 重置生命
     */
    public void resetHealth() {
        this.entityHealthComponent.setHealth(this.entityHealthComponent.getMaxHealth());
        this.entityHealthComponent.setDead(false);

        refreshEntityHealthAttribute();

        if (this.own instanceof Player) {
            Player player = (Player) this.own;

            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            updateAttributesPacket.setEntityId(player.getRuntimeId());
            updateAttributesPacket.setAttributes(new EntityAttribute[]{this.getEntityHealthAttribute(), this.getEntityAbsorptionAttribute()});

            networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
        }
    }

    public void healing(float heal) {
        this.entityHealthAutoUpdateComponent.setLastHealingTime(System.currentTimeMillis());

        float health = this.entityHealthComponent.getHealth() + heal;
        if (health > this.entityHealthComponent.getMaxHealth()) {
            health = this.entityHealthComponent.getMaxHealth();
        }

        this.entityHealthComponent.setHealth(health);

        refreshEntityHealthAttribute();

        if (this.own instanceof Player) {
            Player player = (Player) this.own;

            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            updateAttributesPacket.setEntityId(player.getRuntimeId());
            updateAttributesPacket.setAttributes(new EntityAttribute[]{this.getEntityHealthAttribute(), this.getEntityAbsorptionAttribute()});

            networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
        }
    }

    /**
     * 伤害生物
     *
     * @param damage
     * @return
     */
    public void applyDamage(float damage) {
        this.entityHealthComponent.setLastDamageTime(System.currentTimeMillis());

        // 如果有护盾则先扣护盾
        if (this.entityHealthComponent.getAbsorption() > 0f) {
            float leftAbsorption = this.entityHealthComponent.getAbsorption() - damage;
            if (leftAbsorption >= 0f) {
                this.entityHealthComponent.setAbsorption(leftAbsorption);
                this.refreshEntityAbsorptionAttribute();
                return;
            } else {
                this.entityHealthComponent.setAbsorption(0);
                this.refreshEntityAbsorptionAttribute();
                damage = -leftAbsorption;
            }
        }
        // 再扣血
        float leftHealth = this.entityHealthComponent.getHealth() - damage;
        if (leftHealth >= 1.0) {
            this.entityHealthComponent.setHealth(leftHealth);
            this.refreshEntityHealthAttribute();
        } else {
            this.entityHealthComponent.setHealth(0);
            this.refreshEntityHealthAttribute();
            this.entityHealthComponent.setDead(true);
        }
    }

    /**
     * 对生物造成伤害
     *
     * @param damage
     * @param damageType 伤害类型
     * @param damager    击杀者，可能为null，根据伤害类型
     */
    public void damageEntity(float damage, EntityDamageType damageType, Entity damager) {
        // 计算攻击间隔
        if (!this.canDamaged()) {
            // 处于无敌时间内
            return;
        }

        Entity entity = (Entity) this.own;

        // 发送事件
        EntityDamagedEvent entityDamagedEvent = new EntityDamagedEvent(entity, damage, damageType, damager);
        EventDispatcher.getInstance().dispatchEvent(entityDamagedEvent);
        if (entityDamagedEvent.isCancelled()) {
            return;
        }

        // 动画效果
        entityAnimationService.hurt(entity);

        // 计算伤害
        this.applyDamage(damage);
        if (this.isAlive()) {
            if (entity instanceof Player) {
                UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                updateAttributesPacket.setEntityId(entity.getRuntimeId());
                updateAttributesPacket.setAttributes(new EntityAttribute[]{this.getEntityHealthAttribute(), this.getEntityAbsorptionAttribute()});

                networkManager.sendMessage(((Player) entity).getClientAddress(), updateAttributesPacket);
            }
        } else {
            killEntity(entity, damager);
        }
    }

    public static void killEntity(Entity entity, Entity damager) {
        EntityDeathEvent entityDeathEvent = new EntityDeathEvent(entity, damager);
        EventDispatcher.getInstance().dispatchEvent(entityDeathEvent);

        EntityEventPacket entityEventPacket = new EntityEventPacket();
        entityEventPacket.setEntityRuntimeId(entity.getRuntimeId());
        entityEventPacket.setEventId(EntityEventPacket.DEATH);

        if (entity instanceof Player) {
            broadcastServiceProxy.broadcast(entity, entityEventPacket, true);

            BroadcastModule broadcastModule = BROADCAST_MODULE_ECS_MODULE_HANDLER.removeModule(entity);
            SceneManager.getInstance().clearSubscriberData(broadcastModule.getBroadcaster());
            broadcastModule.setCurrentGrid(null);
        }

        if (entity instanceof MobEntity) {
            broadcastServiceProxy.broadcast(entity, entityEventPacket);
            mobEntityService.onEntityDeath((MobEntity) entity);
        }

        if (entity instanceof MonsterEntity) {
            broadcastServiceProxy.broadcast(entity, entityEventPacket);
            monsterEntityService.onEntityDeath((MonsterEntity) entity);
        }
    }

    // ----- 属性数据获取 -----
    public EntityAttribute getEntityHealthAttribute() {
        EntityAttribute entityAttribute = this.entityAttributeDataComponent.getEntityAttributeMap().get(EntityAttributeType.HEALTH);
        if (entityAttribute == null) {
            entityAttribute = refreshEntityHealthAttribute();
        }

        return entityAttribute;
    }

    public EntityAttribute getEntityAbsorptionAttribute() {
        EntityAttribute entityAttribute = this.entityAttributeDataComponent.getEntityAttributeMap().get(EntityAttributeType.ABSORPTION);
        if (entityAttribute == null) {
            entityAttribute = refreshEntityAbsorptionAttribute();
        }

        return entityAttribute;
    }

    public EntityAttribute refreshEntityHealthAttribute() {
        Map<EntityAttributeType, EntityAttribute> entityAttributeMap = this.entityAttributeDataComponent.getEntityAttributeMap();
        EntityAttribute entityAttribute = entityAttributeMap.get(EntityAttributeType.HEALTH);
        if (entityAttribute == null) {
            entityAttribute = new EntityAttribute(EntityAttributeType.HEALTH,
                    0,
                    this.entityHealthComponent.getMaxHealth(),
                    this.entityHealthComponent.getHealth(),
                    0);

            entityAttributeMap.put(EntityAttributeType.HEALTH, entityAttribute);
        }
        entityAttribute.setCurrentValue(this.entityHealthComponent.getHealth());
        entityAttribute.setMaxValue(this.entityHealthComponent.getMaxHealth());

        return entityAttribute;
    }

    public EntityAttribute refreshEntityAbsorptionAttribute() {
        Map<EntityAttributeType, EntityAttribute> entityAttributeMap = this.entityAttributeDataComponent.getEntityAttributeMap();
        EntityAttribute entityAttribute = entityAttributeMap.get(EntityAttributeType.ABSORPTION);
        if (entityAttribute == null) {
            entityAttribute = new EntityAttribute(EntityAttributeType.ABSORPTION,
                    0,
                    this.entityHealthComponent.getMaxAbsorption(),
                    this.entityHealthComponent.getAbsorption(),
                    0);

            entityAttributeMap.put(EntityAttributeType.ABSORPTION, entityAttribute);
        }
        entityAttribute.setCurrentValue(this.entityHealthComponent.getAbsorption());
        entityAttribute.setMaxValue(this.entityHealthComponent.getMaxAbsorption());

        return entityAttribute;
    }

    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return REQUIRE_CLASSES;
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.own = gameObject;

        this.entityAttributeDataComponent = ENTITY_ATTRIBUTE_DATA_COMPONENT_HANDLER.getComponent(gameObject);
        this.entityHealthComponent = ENTITY_HEALTH_COMPONENT_HANDLER.getComponent(gameObject);
        this.entityHealthAutoUpdateComponent = ENTITY_AUTO_HEALING_COMPONENT_HANDLER.getComponent(gameObject);
    }
}
