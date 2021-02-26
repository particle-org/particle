package com.particle.game.entity.service;

import com.particle.api.entity.MonsterEntityServiceApi;
import com.particle.api.entity.attribute.MetaDataServiceApi;
import com.particle.api.entity.attribute.MovementServiceAPI;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attack.EntityAttackService;
import com.particle.game.entity.attack.EntityAttackedHandleService;
import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.attribute.death.DeathExperienceModule;
import com.particle.game.entity.attribute.explevel.ExperienceService;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.attribute.identified.EntityNameModule;
import com.particle.game.entity.attribute.identified.UUIDModule;
import com.particle.game.entity.attribute.metadata.EntityMetaDataModule;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.config.MonsterEntityConfig;
import com.particle.game.entity.service.network.MonsterEntityPacketBuilder;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.spawn.processor.MonsterEntitySpawnProcessor;
import com.particle.game.entity.state.EntityStateService;
import com.particle.game.item.ItemBindModule;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.scene.module.BroadcastModule;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.game.world.physical.EntityColliderService;
import com.particle.game.world.physical.PhysicalService;
import com.particle.game.world.physical.modules.RigibodyModule;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.entity.type.EntityTypeData;
import com.particle.model.entity.type.EntityTypeDictionary;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class MonsterEntityService implements MonsterEntityServiceApi {

    private static Logger LOGGER = LoggerFactory.getLogger(MonsterEntityService.class);

    private static final ECSModuleHandler<EntityNameModule> ENTITY_NAME_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityNameModule.class);

    private static final ECSModuleHandler<EntityMetaDataModule> ENTITY_META_DATA_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMetaDataModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<DeathExperienceModule> DEATH_EXPERIENCE_MODULE_HANDLER = ECSModuleHandler.buildHandler(DeathExperienceModule.class);

    private static final ECSModuleHandler<UUIDModule> UUID_MODULE_HANDLER = ECSModuleHandler.buildHandler(UUIDModule.class);

    private static final ECSModuleHandler<BroadcastModule> BROADCAST_MODULE_HANDLER = ECSModuleHandler.buildHandler(BroadcastModule.class);

    private static final ECSModuleHandler<RigibodyModule> RIGIBODY_MODULE_HANDLER = ECSModuleHandler.buildHandler(RigibodyModule.class);

    private static final ECSModuleHandler<ItemBindModule> ITEM_BIND_MODULE_HANDLER = ECSModuleHandler.buildHandler(ItemBindModule.class);

    @Inject
    private MonsterEntitySpawnProcessor monsterEntitySpawnProcessor;
    @Inject
    private MonsterEntityPacketBuilder monsterEntityPacketBuilder;

    @Inject
    private PhysicalService physicalService;
    @Inject
    private MovementServiceProxy movementServiceProxy;
    @Inject
    private PositionService positionService;
    @Inject
    private EntityColliderService entityColliderService;
    @Inject
    private EntityAttackedHandleService entityAttackedHandleService;
    @Inject
    private HealthServiceProxy healthServiceProxy;
    @Inject
    private EntityAttackService entityAttackService;
    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;
    @Inject
    private ItemEntityService itemEntityService;
    @Inject
    private EntitySpawnService entitySpawnService;
    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;
    @Inject
    private EntityStateService entityStateService;
    @Inject
    private ExperienceService experienceService;
    @Inject
    private MetaDataServiceApi metaDataServiceApi;
    @Inject
    private MovementServiceAPI movementServiceAPI;

    // 背包相关
    @Inject
    private InventoryManager inventoryManager;
    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    // ECS操作
    @Inject
    private ECSComponentManager ecsComponentManager;

    @Inject
    public void init() {
        // 读取配置
        MonsterEntityConfigService.init();
    }

    @Override
    public MonsterEntity createEntity(String actorType, Vector3f position) {
        EntityTypeData entityTypeData = EntityTypeDictionary.getMobEntityConfig(actorType);
        if (entityTypeData == null) {
            return null;
        }
        return this.createEntity(actorType, entityTypeData.getType(), position);
    }

    private MonsterEntity createEntity(String actorType, int networkId, Vector3f position) {
        MonsterEntity monsterEntity = new MonsterEntity(actorType, networkId);
        monsterEntity.init();

        MonsterEntityConfig monsterEntityConfig = MonsterEntityConfigService.getMonsterEntityConfig(actorType);

        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(monsterEntity);
        transformModule.setPosition(position);
        transformModule.setDirection(0, 0, 0);
        transformModule.setMoveEntityPacketBuilder(this.monsterEntityPacketBuilder.getMovePacketBuilder(monsterEntity));

        // 移动相关
        this.movementServiceProxy.enableMovement(monsterEntity, monsterEntityConfig.getSpeed(), monsterEntityConfig.getMaxSpeed());

        // Spawn相关业务
        this.entitySpawnService.enableSpawn(
                monsterEntity,
                this.monsterEntitySpawnProcessor.getEntitySpawnProcessor(monsterEntity),
                this.monsterEntityPacketBuilder.getAddPacketBuilder(monsterEntity),
                this.monsterEntityPacketBuilder.getRemovePacketBuilder(monsterEntity)
        );

        // 订阅相关业务
        BROADCAST_MODULE_HANDLER.bindModule(monsterEntity);

        // 设置Entity基础信息
        EntityMetaDataModule entityMetaDataModule = ENTITY_META_DATA_MODULE_HANDLER.bindModule(monsterEntity);
        entityMetaDataModule.setLongData(EntityMetadataType.FLAGS, 0);
        entityMetaDataModule.setFloatData(EntityMetadataType.SCALE, 1.0f);

        this.healthServiceProxy.initHealthComponent(monsterEntity, monsterEntityConfig.getHealth());

        // UUID相关业务
        UUIDModule uuidModule = UUID_MODULE_HANDLER.bindModule(monsterEntity);
        uuidModule.setUuid(UUID.randomUUID());

        ENTITY_NAME_MODULE_HANDLER.bindModule(monsterEntity);

        ITEM_BIND_MODULE_HANDLER.bindModule(monsterEntity);

        this.entityStateService.initEntityStateComponent(monsterEntity);

        //更新移动
        this.movementServiceAPI.updateMovement(monsterEntity, monsterEntityConfig.getSpeed(), monsterEntityConfig.getMaxSpeed());

        // 物理引擎
        this.setBoundingBox(monsterEntity, position, monsterEntityConfig.getWidth(), monsterEntityConfig.getHeight());

        this.entityColliderService.bindDefaultColliderDetector(monsterEntity);

        this.physicalService.initPhysicalEffects(monsterEntity, false, !(monsterEntityConfig.getGravity() == 0), monsterEntityConfig.getGravity());

        // 伤害相关业务
        this.entityAttackService.initEntityAttackComponent(monsterEntity, monsterEntityConfig.getDamage(), 1000);
        this.entityRemoteAttackService.initEntityRemoteAttackComponent(monsterEntity, 3000);
        this.entityAttackedHandleService.initEntityAttackedComponent(monsterEntity);

        // 设置死亡经验
        this.setDeathExperience(monsterEntity, monsterEntityConfig.getExperience());

        // 配置system
        this.ecsComponentManager.filterTickedSystem(monsterEntity);

        return monsterEntity;
    }

    public void onEntityDeath(MonsterEntity entity) {
        MonsterEntityConfig monsterEntityConfig = MonsterEntityConfigService.getMonsterEntityConfig(entity.getActorType());

        if (monsterEntityConfig != null) {
            // 计算经验
            Player entityAttacker = this.entityAttackedHandleService.getEntityAttacker(entity);
            if (entityAttacker != null) {
                DeathExperienceModule module = DEATH_EXPERIENCE_MODULE_HANDLER.getModule(entity);

                if (module != null) {
                    this.experienceService.addExperience(entityAttacker, module.getExperience(), false);
                }
            }
        }

        entity.getLevel().getLevelSchedule().scheduleDelayTask("EntityDeathDespawn", () -> this.entitySpawnService.despawn(entity), 1000);
        this.entityDecisionServiceProxy.resetActionDecision(entity);
    }

    @Override
    public UUID getMonsterEntityUuid(MonsterEntity monsterEntity) {
        UUIDModule module = UUID_MODULE_HANDLER.getModule(monsterEntity);
        if (module != null) {
            return module.getUuid();
        }

        return null;
    }

    /**
     * 设置生物的宽度和高度
     */
    private void setBoundingBox(Entity entity, Vector3f position, float width, float height) {
        // 物理引擎
        Vector3f center = new Vector3f(0, height / 2, 0);
        Vector3f size = new Vector3f(width, height, width);

        this.entityColliderService.bindAABBBindBox(entity, position, center, size);

        metaDataServiceApi.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_HAS_COLLISION, true, true);

        metaDataServiceApi.setFloatData(entity, EntityMetadataType.BOUNDING_BOX_WIDTH, width, true);
        metaDataServiceApi.setFloatData(entity, EntityMetadataType.BOUNDING_BOX_HEIGHT, height, true);
    }

    /**
     * 设置怪物的体型的大小
     */
    private void setScale(Entity entity, float scaleRatio) {
        // 变化是依据当前尺寸情况，以更好处理巨大和缩小叠加的问题
        float scale = metaDataServiceApi.getFloatData(entity, EntityMetadataType.SCALE);

        metaDataServiceApi.setFloatData(entity, EntityMetadataType.SCALE, scale * scaleRatio, true);
        metaDataServiceApi.refreshMetadata(entity);
    }

    @Override
    public void setDeathExperience(Entity entity, int amount) {
        DeathExperienceModule experienceComponent = DEATH_EXPERIENCE_MODULE_HANDLER.bindModule(entity);
        experienceComponent.setExperience(amount);
    }
}
