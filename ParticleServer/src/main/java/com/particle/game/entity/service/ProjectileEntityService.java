package com.particle.game.entity.service;

import com.particle.api.entity.IProjectileEntityServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attack.component.ProjectileAttackModule;
import com.particle.game.entity.attribute.metadata.EntityMetaDataModule;
import com.particle.game.entity.movement.module.AutoDirectionModule;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.entity.service.network.ProjectileEntityPacketBuilder;
import com.particle.game.entity.service.template.EntityTemplateService;
import com.particle.game.entity.spawn.SpawnModule;
import com.particle.game.entity.spawn.processor.ProjectileEntitySpawnProcessor;
import com.particle.game.item.RemoteWeaponConfig;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.game.world.physical.EntityColliderService;
import com.particle.game.world.physical.PhysicalService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.EntityType;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.model.projectile.*;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.util.configer.ConfigServiceProvider;
import com.particle.util.configer.IConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ProjectileEntityService implements IProjectileEntityServiceApi {

    private static Logger LOGGER = LoggerFactory.getLogger(ProjectileEntityService.class);

    private static final ECSModuleHandler<EntityMetaDataModule> ENTITY_META_DATA_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMetaDataModule.class);

    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<AutoDirectionModule> AUTO_DIRECTION_MODULE_HANDLER = ECSModuleHandler.buildHandler(AutoDirectionModule.class);

    private static final ECSModuleHandler<SpawnModule> SPAWN_MODULE_HANDLER = ECSModuleHandler.buildHandler(SpawnModule.class);

    private static final ECSModuleHandler<ProjectileAttackModule> PROJECTILE_ATTACK_MODULE_HANDLER = ECSModuleHandler.buildHandler(ProjectileAttackModule.class);


    @Inject
    private ProjectileEntitySpawnProcessor projectileEntitySpawnProcessor;
    @Inject
    private ProjectileEntityPacketBuilder projectileEntityPacketBuilder;

    @Inject
    private ECSComponentManager ecsComponentManager;

    @Inject
    private PhysicalService physicalService;
    @Inject
    private EntityColliderService entityColliderService;

    @Inject
    private EntityTemplateService entityTemplateService;

    private IConfigService configService = ConfigServiceProvider.getConfigService();

    private Map<String, RemoteWeaponConfig> remoteWeaponConfigs = new HashMap<>();

    private Map<Integer, Class<? extends ProjectileEntity>> entityDictionary = new HashMap<>();
    private Map<ItemPrototype, Integer> projectileRecorder = new HashMap<>();

    @Inject
    public void initEntities() {
        this.entityDictionary.put(ProjectileEntity.EYE_OF_ENDER, EyeOfEnder.class);
        this.entityDictionary.put(ProjectileEntity.FIREWORK_ROCKET, FireworksRocket.class);
        this.entityDictionary.put(ProjectileEntity.TRIDENT, Trident.class);
        this.entityDictionary.put(ProjectileEntity.SHULKER_BULLET, ShulkerBullet.class);
        this.entityDictionary.put(ProjectileEntity.DRAGON_FIREBALL, DragonFireball.class);
        this.entityDictionary.put(ProjectileEntity.ARROW, Arrow.class);
        this.entityDictionary.put(ProjectileEntity.SNOWBALL, ThrownSnowball.class);
        this.entityDictionary.put(ProjectileEntity.EGG, ThrownEgg.class);
        this.entityDictionary.put(ProjectileEntity.LARGE_FIREBALL, LargeFireball.class);
        this.entityDictionary.put(ProjectileEntity.SPLASH_POTION, SplashPotion.class);
        this.entityDictionary.put(ProjectileEntity.ENDER_PEARL, EnderPearl.class);
        this.entityDictionary.put(ProjectileEntity.SMALL_FIREBALL, SmallFireball.class);
        this.entityDictionary.put(ProjectileEntity.LINGERING_POTION, LingeringPotion.class);
    }

    @Inject
    public void initConfig() {
        List<RemoteWeaponConfig> remoteWeaponConfigs = this.configService.loadConfigsOrSaveDefault(RemoteWeaponConfig.class);
        for (RemoteWeaponConfig remoteWeaponConfig : remoteWeaponConfigs) {
            this.remoteWeaponConfigs.put(remoteWeaponConfig.getId(), remoteWeaponConfig);
        }
    }

    @Inject
    private void initItemMap() {
        // 抛射物
        this.projectileRecorder.put(ItemPrototype.ARROW, ProjectileEntity.ARROW);
        // 保留火箭，因為弓箭可被使用
        this.projectileRecorder.put(ItemPrototype.FIREWORKS, ProjectileEntity.FIREWORK_ROCKET);
        this.projectileRecorder.put(ItemPrototype.TRIDENT, ProjectileEntity.TRIDENT);
        this.projectileRecorder.put(ItemPrototype.FIREBALL, ProjectileEntity.SMALL_FIREBALL);
        this.projectileRecorder.put(ItemPrototype.TNT, ProjectileEntity.LARGE_FIREBALL);
        this.projectileRecorder.put(ItemPrototype.SNOWBALL, ProjectileEntity.SNOWBALL);
        this.projectileRecorder.put(ItemPrototype.SPLASH_POTION, ProjectileEntity.SPLASH_POTION);
        this.projectileRecorder.put(ItemPrototype.ENDER_PEARL, ProjectileEntity.ENDER_PEARL);
        this.projectileRecorder.put(ItemPrototype.EGG, ProjectileEntity.EGG);
    }

    @Override
    public Entity createEntity(String id, ItemStack weapon, Vector3f position, Vector3f motion) {
        RemoteWeaponConfig remoteWeaponConfig = this.remoteWeaponConfigs.get(id);
        if (remoteWeaponConfig == null) {
            LOGGER.warn("Remote weapon config {} not exist", id);

            return null;
        }

        Entity projectileEntity = this.entityTemplateService.createEntityFromTemplate(remoteWeaponConfig.getEntityTemplate(), position);

        // 位置相关信息
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(projectileEntity);
        transformModule.setPosition(position);

        Direction direction = new Direction(motion);
        if (projectileEntity instanceof Arrow || projectileEntity instanceof Trident || projectileEntity instanceof FireworksRocket) {
            direction.setYaw(-direction.getYaw());
            direction.setPitch(-direction.getPitch());
        }
        transformModule.setDirection(direction);
        transformModule.setMoveEntityPacketBuilder(this.projectileEntityPacketBuilder.getMovePacketBuilder(projectileEntity));

        // 移动组件
        ENTITY_MOVEMENT_MODULE_HANDLER.bindModule(projectileEntity);

        // 设置Entity基础信息
        EntityMetaDataModule entityMetaDataModule = ENTITY_META_DATA_MODULE_HANDLER.bindModule(projectileEntity);
        entityMetaDataModule.setLongData(EntityMetadataType.FLAGS, 0);
        entityMetaDataModule.setFloatData(EntityMetadataType.SCALE, 1f);

        // 标记接入物理引擎
        this.entityColliderService.bindAABBBindBox(
                projectileEntity,
                transformModule.getPosition(),
                new Vector3f(0, 0, 0),
                new Vector3f(
                        remoteWeaponConfig.getBoundBoxX(),
                        remoteWeaponConfig.getBoundBoxY(),
                        remoteWeaponConfig.getBoundBoxZ()));
        this.entityColliderService.bindRadiationDetectionComponent(projectileEntity);
        this.physicalService.initPhysicalEffects(
                projectileEntity,
                false,
                remoteWeaponConfig.getGravity() != 0,
                remoteWeaponConfig.getGravity());

        //允许Spawn到世界
        SpawnModule spawnModule = SPAWN_MODULE_HANDLER.bindModule(projectileEntity);
        spawnModule.setSpawnEntityProcessor(this.projectileEntitySpawnProcessor.getEntitySpawnProcessor(projectileEntity));
        spawnModule.setAddEntityPacketBuilder(this.projectileEntityPacketBuilder.getAddPacketBuilder(projectileEntity));
        spawnModule.setRemoveEntityPacketBuilder(this.projectileEntityPacketBuilder.getRemovePacketBuilder(projectileEntity));

        // 绑定远程攻击组件
        ProjectileAttackModule projectileAttackModule = PROJECTILE_ATTACK_MODULE_HANDLER.bindModule(projectileEntity);
        projectileAttackModule.setWeapon(weapon);
        projectileAttackModule.setBaseDamage(remoteWeaponConfig.getDamage());
        projectileAttackModule.setAdditionDamage(remoteWeaponConfig.getAdditionDamage());
        projectileAttackModule.setAdditionDamageSpeed(remoteWeaponConfig.getAdditionDamageSpeed());
        projectileAttackModule.setHasExplosion(remoteWeaponConfig.isExplosion());
        projectileAttackModule.setExplosionDamage(remoteWeaponConfig.getExplosionPower());

        //配置system
        ecsComponentManager.filterTickedSystem(projectileEntity);

        // 设置朝向
        if (remoteWeaponConfig.isAutoDirection()) {
            AUTO_DIRECTION_MODULE_HANDLER.bindModule(projectileEntity);
        }

        return projectileEntity;
    }

    public ProjectileEntity createEntity(ItemStack itemstack, Vector3f position, Vector3f motion) {
        Integer entityId = this.projectileRecorder.get(itemstack.getItemType());
        if (entityId == null) {
            LOGGER.warn("Weapon shoot {} without entity bind", itemstack.getItemType().getName());

            return null;
        }

        return this.createEntity(entityId, position, motion);
    }

    public ProjectileEntity createEntity(int networkId, Vector3f position, Vector3f motion) {
        Class<? extends ProjectileEntity> entityClass = this.entityDictionary.get(networkId);
        if (entityClass == null) {
            return null;
        }

        ProjectileEntity projectileEntity = null;
        try {
            projectileEntity = entityClass.newInstance();
            projectileEntity.init();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Fail to create entity instance!", e);
            return null;
        }

        // 位置相关信息
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(projectileEntity);
        transformModule.setPosition(position);

        Direction direction = new Direction(motion);
        if (projectileEntity instanceof Arrow || projectileEntity instanceof Trident || projectileEntity instanceof FireworksRocket) {
            direction.setYaw(-direction.getYaw());
            direction.setPitch(-direction.getPitch());
        }
        transformModule.setDirection(direction);
        transformModule.setMoveEntityPacketBuilder(this.projectileEntityPacketBuilder.getMovePacketBuilder(projectileEntity));

        this.convertEntityToProjectile(projectileEntity);

        // 设置朝向
        if (networkId == EntityType.ARROW.type() || networkId == EntityType.THROWN_TRIDENT.type() || networkId == EntityType.FIREWORKS_ROCKET.type()) {
            AUTO_DIRECTION_MODULE_HANDLER.bindModule(projectileEntity);
        }

        return projectileEntity;
    }

    public void convertEntityToProjectile(Entity projectileEntity) {
        // 位置相关信息
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(projectileEntity);
        transformModule.setMoveEntityPacketBuilder(this.projectileEntityPacketBuilder.getMovePacketBuilder(projectileEntity));

        // 移动组件
        ENTITY_MOVEMENT_MODULE_HANDLER.bindModule(projectileEntity);

        // 设置Entity基础信息
        EntityMetaDataModule entityMetaDataModule = ENTITY_META_DATA_MODULE_HANDLER.bindModule(projectileEntity);
        entityMetaDataModule.setLongData(EntityMetadataType.FLAGS, 0);
        entityMetaDataModule.setFloatData(EntityMetadataType.SCALE, 1f);

        // 标记接入物理引擎
        this.entityColliderService.bindAABBBindBox(projectileEntity, transformModule.getPosition(), new Vector3f(0, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f));
        this.entityColliderService.bindRadiationDetectionComponent(projectileEntity);
        this.physicalService.initPhysicalEffects(projectileEntity, false, !projectileEntity.canFly());

        //允许Spawn到世界
        SpawnModule spawnModule = SPAWN_MODULE_HANDLER.bindModule(projectileEntity);
        spawnModule.setSpawnEntityProcessor(this.projectileEntitySpawnProcessor.getEntitySpawnProcessor(projectileEntity));
        spawnModule.setAddEntityPacketBuilder(this.projectileEntityPacketBuilder.getAddPacketBuilder(projectileEntity));
        spawnModule.setRemoveEntityPacketBuilder(this.projectileEntityPacketBuilder.getRemovePacketBuilder(projectileEntity));

        //配置system
        ecsComponentManager.filterTickedSystem(projectileEntity);
    }

}
