package com.particle.game.entity.attack;

import com.particle.api.entity.IEntityRemoteAttackServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.attack.component.EntityRemoteAttackModule;
import com.particle.game.entity.attack.component.ProjectileAttackModule;
import com.particle.game.entity.attack.processor.*;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.ProjectileEntityService;
import com.particle.game.entity.spawn.AutoRemovedModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.state.EntityStateService;
import com.particle.game.item.DurabilityService;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.world.physical.EntityColliderService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.entity.EntityPiercingModule;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.model.projectile.ProjectileEntity;
import com.particle.model.events.level.entity.EntityDamageByEntityEvent;
import com.particle.model.events.level.entity.EntityDamageType;
import com.particle.model.events.level.entity.EntityRemoteAttackEvent;
import com.particle.model.events.level.entity.ProjectileHitEntityEvent;
import com.particle.model.inventory.ArmorInventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantment;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Singleton
public class EntityRemoteAttackService implements IEntityRemoteAttackServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRemoteAttackService.class);

    private static final ECSModuleHandler<EntityPiercingModule> ENTITY_PIERCING_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityPiercingModule.class);

    private static final ECSModuleHandler<EntityRemoteAttackModule> ENTITY_REMOTE_ATTACK_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityRemoteAttackModule.class);

    private static final ECSModuleHandler<ProjectileAttackModule> PROJECTILE_ATTACK_MODULE_HANDLER = ECSModuleHandler.buildHandler(ProjectileAttackModule.class);

    private static final ECSModuleHandler<AutoRemovedModule> AUTO_REMOVED_MODULE_HANDLER = ECSModuleHandler.buildHandler(AutoRemovedModule.class);


    @Inject
    private MovementServiceProxy movementServiceProxy;
    @Inject
    private DurabilityService durabilityService;
    @Inject
    private PositionService positionService;
    @Inject
    private ProjectileEntityService projectileEntityService;
    @Inject
    private EntitySpawnService entitySpawnService;
    @Inject
    private EntityDefenseService entityDefenseService;
    @Inject
    private EntityKnockBackService entityKnockBackService;
    @Inject
    private HealthServiceProxy healthServiceProxy;
    @Inject
    private MetaDataService metaDataService;
    @Inject
    private EntityStateService entityStateService;

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;
    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private EntityAttackedHandleService entityAttackedHandleService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();


    // 攻击处理器
    @Inject
    private EntityColliderService entityColliderService;

    private Map<Integer, IHitEntityProcessor> projectileCallbackRecorder = new HashMap<>();

    @Inject
    private CommonExplosionHitProcessor commonExplosionHitProcessor;
    @Inject
    private CommonSingleHitProcessor commonSingleHitProcessor;

    @Inject
    private void init(ArrowHitEntityProcessor arrowHitEntityProcessor,
                      SmallFireBallEntityProcessor smallFireBallEntityProcessor,
                      BigFireBallEntityProcessor bigFireBallEntityProcessor,
                      SnowBallEntityProcessor snowBallEntityProcessor,
                      SplashPotionProcessor splashPotionProcessor,
                      EnderPearHitProcessor enderPearHitProcessor,
                      EggEntityProcessor eggHitProcessor,
                      FireworkRocketEntityProcessor fireworkRocketEntityProcessor,
                      TridentHitEntityProcessor tridentHitEntityProcessor) {
        // 处理器
        this.projectileCallbackRecorder.put(ProjectileEntity.ARROW, arrowHitEntityProcessor);
        this.projectileCallbackRecorder.put(ProjectileEntity.TRIDENT, tridentHitEntityProcessor);
        this.projectileCallbackRecorder.put(ProjectileEntity.SMALL_FIREBALL, smallFireBallEntityProcessor);
        this.projectileCallbackRecorder.put(ProjectileEntity.LARGE_FIREBALL, bigFireBallEntityProcessor);
        this.projectileCallbackRecorder.put(ProjectileEntity.SNOWBALL, snowBallEntityProcessor);
        this.projectileCallbackRecorder.put(ProjectileEntity.SPLASH_POTION, splashPotionProcessor);
        this.projectileCallbackRecorder.put(ProjectileEntity.ENDER_PEARL, enderPearHitProcessor);
        this.projectileCallbackRecorder.put(ProjectileEntity.EGG, eggHitProcessor);
        this.projectileCallbackRecorder.put(ProjectileEntity.FIREWORK_ROCKET, fireworkRocketEntityProcessor);
    }

    /**
     * 初始化生物远程攻击组件
     *
     * @param entity
     */
    public void initEntityRemoteAttackComponent(Entity entity) {
        this.initEntityRemoteAttackComponent(entity, 1000);
    }

    /**
     * 初始化生物远程攻击组件
     *
     * @param entity
     * @param attackInterval
     */
    @Override
    public void initEntityRemoteAttackComponent(Entity entity, long attackInterval) {
        EntityRemoteAttackModule entityRemoteAttackModule = ENTITY_REMOTE_ATTACK_MODULE_HANDLER.bindModule(entity);
        entityRemoteAttackModule.setAttackInterval(attackInterval);
    }

    @Override
    public void initEntityRemoteAttackComponent(Entity entity, long attackInterval, float damageRate) {
        EntityRemoteAttackModule entityRemoteAttackModule = ENTITY_REMOTE_ATTACK_MODULE_HANDLER.bindModule(entity);
        entityRemoteAttackModule.setAttackInterval(attackInterval);
        entityRemoteAttackModule.setDamageRate(damageRate);
    }

    /**
     * 生物攻击间隔检查
     *
     * @param entity
     * @return
     */
    public boolean entityRemoteAttackCheck(Entity entity) {
        EntityRemoteAttackModule entityRemoteAttackModule = ENTITY_REMOTE_ATTACK_MODULE_HANDLER.getModule(entity);
        if (entityRemoteAttackModule == null) {
            return false;
        }
        return System.currentTimeMillis() - entityRemoteAttackModule.getLastAttackTimestamp() >= entityRemoteAttackModule.getAttackInterval();
    }

    /**
     * 创建并刷新抛射出
     *
     * @param entity
     * @param weapon
     * @param motion
     * @return
     */
    @Override
    public Entity projectileShoot(Entity entity, ItemStack weapon, Vector3f motion) {
        // Step 1 : 创建抛射物
        ProjectileEntity projectileEntity = this.projectileEntityService.createEntity(weapon, this.positionService.getPosition(entity).add(0, 1.62f, 0), motion);
        if (projectileEntity == null) {
            LOGGER.warn("Projectile weapon {} without entity bind", weapon);

            return null;
        }
        if (weapon.getItemType() == ItemPrototype.SPLASH_POTION) {
            this.metaDataService.setShortData(projectileEntity, EntityMetadataType.POTION_AUX_VALUE, (short) weapon.getMeta());
        }

        // Step 2 : 配置抛射物
        IHitEntityProcessor iHitEntityProcessor = this.projectileCallbackRecorder.get(projectileEntity.getNetworkId());
        if (iHitEntityProcessor == null) {
            return null;
        }

        Consumer<Vector3f> blockColliderCallback = iHitEntityProcessor.getColliderBlockCallback(entity, weapon, projectileEntity);
        if (blockColliderCallback != null) {
            this.entityColliderService.updateColliderBlockCallback(projectileEntity, blockColliderCallback);
        }
        Consumer<Entity> entityColliderCallback = iHitEntityProcessor.getColliderEntityCallback(entity, weapon, projectileEntity);
        if (entityColliderCallback != null) {
            this.entityColliderService.updateColliderEntityCallback(projectileEntity, entityColliderCallback);
        }

        // 绑定远程攻击组件
        ProjectileAttackModule projectileAttackModule = PROJECTILE_ATTACK_MODULE_HANDLER.bindModule(projectileEntity);
        projectileAttackModule.setWeapon(weapon);
        projectileAttackModule.setBaseDamage(1f);
        projectileAttackModule.setAdditionDamage(5f);
        projectileAttackModule.setAdditionDamageSpeed(18);

        // Step 3: 发射生物
        this.projectileEntityShoot(
                entity,
                projectileEntity,
                motion);

        return projectileEntity;
    }

    /**
     * 通用生物射击接口
     *
     * @param sourceEntity
     * @param projectileEntity
     * @param motion
     */
    @Override
    public void commonProjectileEntityShoot(Entity sourceEntity, Entity projectileEntity, Vector3f motion) {
        ProjectileAttackModule projectileAttackModule = PROJECTILE_ATTACK_MODULE_HANDLER.getModule(projectileEntity);

        if (projectileAttackModule.isHasExplosion()) {
            this.entityColliderService.updateColliderBlockCallback(projectileEntity, this.commonExplosionHitProcessor.getColliderBlockCallback(sourceEntity, projectileAttackModule.getWeapon(), projectileEntity));
            this.entityColliderService.updateColliderEntityCallback(projectileEntity, this.commonExplosionHitProcessor.getColliderEntityCallback(sourceEntity, projectileAttackModule.getWeapon(), projectileEntity));
        } else {
            this.entityColliderService.updateColliderBlockCallback(projectileEntity, this.commonSingleHitProcessor.getColliderBlockCallback(sourceEntity, projectileAttackModule.getWeapon(), projectileEntity));
            this.entityColliderService.updateColliderEntityCallback(projectileEntity, this.commonSingleHitProcessor.getColliderEntityCallback(sourceEntity, projectileAttackModule.getWeapon(), projectileEntity));
        }

        // 发射生物
        this.projectileEntityShoot(
                sourceEntity,
                projectileEntity,
                motion);
    }

    /**
     * 抛射生物
     *
     * @param sourceEntity
     * @param projectileEntity
     * @param motion
     */
    @Override
    public void projectileEntityShoot(Entity sourceEntity, Entity projectileEntity, Vector3f motion) {
        // 刷新攻击间隔
        EntityRemoteAttackModule entityRemoteAttackModule = ENTITY_REMOTE_ATTACK_MODULE_HANDLER.getModule(sourceEntity);
        if (entityRemoteAttackModule == null) {
            return;
        }

        entityRemoteAttackModule.setLastAttackTimestamp(System.currentTimeMillis());

        // 火矢附魔效果
        boolean isFlame = isEnchantmentFlame(sourceEntity);
        if (isFlame) {
            this.metaDataService.setDataFlag(projectileEntity, MetadataDataFlag.DATA_FLAG_ONFIRE, true, false);
        }

        // 发送事件
        EntityRemoteAttackEvent entityRemoteAttackEvent = new EntityRemoteAttackEvent(sourceEntity, motion);
        this.eventDispatcher.dispatchEvent(entityRemoteAttackEvent);
        if (entityRemoteAttackEvent.isCancelled()) {
            return;
        }

        // 配置抛射物
        this.movementServiceProxy.setMotion(projectileEntity, motion);

        // 刷新抛射物
        this.entitySpawnService.spawn(sourceEntity.getLevel(), projectileEntity);
    }

    /**
     * 生物被抛射物击中处理
     *
     * @param source
     * @param victim
     * @param projectile
     * @param weapon
     */
    public void projectileHitEntity(Entity source, Entity victim, Entity projectile, ItemStack weapon) {
        // 添加抛射物射中Entity事件，可让其他模块取消事件
        ProjectileHitEntityEvent projectileHitEntityEvent = new ProjectileHitEntityEvent(source, victim, projectile, weapon);
        this.eventDispatcher.dispatchEvent(projectileHitEntityEvent);

        if (projectileHitEntityEvent.isCancelled()) {
            projectile.getLevel().getLevelSchedule().scheduleDelayTask("ProjectileHit", () -> {
                this.entitySpawnService.despawn(projectile);
            }, 100);

            return;
        }

        EntityRemoteAttackModule entityRemoteAttackModule = ENTITY_REMOTE_ATTACK_MODULE_HANDLER.getModule(source);
        if (entityRemoteAttackModule == null) {
            return;
        }

        // Step 1 : 伤害计算
        float damage = this.caculateRemoteCombatDamage(projectile, victim, weapon) * entityRemoteAttackModule.getDamageRate();

        // Step 2 : 击退计算
        Vector3f knockback = this.entityKnockBackService.getProjectileKnockback(projectile, victim, weapon);

        // Step 3 : 构造Level层同步事件
        EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(victim);
        entityDamageByEntityEvent.setDamage(damage);
        entityDamageByEntityEvent.setKnockback(knockback);
        entityDamageByEntityEvent.setDamager(source);
        entityDamageByEntityEvent.overrideAfterEventExecuted(() -> {
            // Step 4 : 伤害应用
            this.healthServiceProxy.damageEntity(victim, damage, EntityDamageType.Projectile, source);
            this.movementServiceProxy.setMotion(victim, knockback);

            // Step 5 : 耐久计算
            if (victim instanceof Player) {
                this.durabilityService.consumptionEquipments(victim);
            }

            // 受击者做受击反应
            this.entityAttackedHandleService.entityAttackedByEntity(source, victim);
        });

        this.eventDispatcher.dispatchEvent(entityDamageByEntityEvent);

        // 若是三叉戟
        if (weapon.getItemType() == ItemPrototype.TRIDENT) {
            return;
        }

        // 若有穿透 component 且未超過次數
        EntityPiercingModule entityPiercingModule = ENTITY_PIERCING_MODULE_HANDLER.getModule(source);
        if (entityPiercingModule != null && entityPiercingModule.getPiercingCount() < entityPiercingModule.getMaxCount()) {
            entityPiercingModule.setPiercingCount(entityPiercingModule.getPiercingCount() + 1);
            return;
        }

        // 处理despawn
        projectile.getLevel().getLevelSchedule().scheduleDelayTask("ArrowHit", () -> {
            this.entitySpawnService.despawn(projectile);
        }, 100);
    }

    /**
     * 计算远程攻击伤害
     *
     * @param projectile
     * @param victim
     * @param weapon
     * @return
     */
    private float caculateRemoteCombatDamage(Entity projectile, Entity victim, ItemStack weapon) {
        // 获取基础攻伤害
        float damage = this.caculateProjectileWeaponDamage(projectile, weapon);

        // 计算护甲减伤效果
        // 获取背包
        ArmorInventory armorInventory = (ArmorInventory) inventoryManager.getInventoryByContainerId(victim, InventoryConstants.CONTAINER_ID_ARMOR);
        if (armorInventory != null) {
            // 计算头盔防护
            ItemStack helmet = this.playerInventoryAPI.getItem(armorInventory, ArmorInventory.HELMET);
            ItemStack chestplate = this.playerInventoryAPI.getItem(armorInventory, ArmorInventory.CHESTPLATE);
            ItemStack leggings = this.playerInventoryAPI.getItem(armorInventory, ArmorInventory.LEGGINGS);
            ItemStack boots = this.playerInventoryAPI.getItem(armorInventory, ArmorInventory.BOOTS);

            damage = this.entityDefenseService.caculateArmorDefense(helmet, chestplate, leggings, boots, damage);
            damage = this.entityDefenseService.caculateProjectileDefense(helmet, chestplate, leggings, boots, damage);
        }


        return damage;
    }


    /**
     * 计算远程武器及附带附魔的伤害
     *
     * @param projectile
     * @param weapon
     * @return
     */
    private float caculateProjectileWeaponDamage(Entity projectile, ItemStack weapon) {
        ProjectileAttackModule projectileAttackModule = PROJECTILE_ATTACK_MODULE_HANDLER.getModule(projectile);

        // 获取基础攻伤害
        float damage = projectileAttackModule.getBaseDamage();

        // 计算武器伤害
        float strength = (float) movementServiceProxy.getMotion(projectile).length();

        if (strength > projectileAttackModule.getAdditionDamageSpeed()) {
            strength = projectileAttackModule.getAdditionDamageSpeed();
        }

        // 计算额外伤害
        damage = damage + ((strength / projectileAttackModule.getAdditionDamageSpeed()) * projectileAttackModule.getAdditionDamage());

        // 计算力量附魔加成
        Enchantment enchantment = ItemAttributeService.getEnchantment(weapon, Enchantments.POWER);
        if (enchantment != null) {
            damage = damage + 0.25f * damage * enchantment.getLevel();
        }

        return damage;
    }

    /**
     * 是否火矢附魔
     *
     * @param source
     */
    private boolean isEnchantmentFlame(Entity source) {
        // 获取手持武器
        PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(source, InventoryConstants.CONTAINER_ID_PLAYER);
        if (inventory != null) {
            ItemStack weapon = this.playerInventoryAPI.getItem(inventory, inventory.getItemInHandle());

            // 火矢效果
            Enchantment fireAspectEnchantment = ItemAttributeService.getEnchantment(weapon, Enchantments.FLAME);
            if (fireAspectEnchantment != null) {
                return true;
            }
        }

        return false;
    }
}
