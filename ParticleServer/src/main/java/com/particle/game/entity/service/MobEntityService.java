package com.particle.game.entity.service;

import com.particle.api.entity.MobEntityServiceApi;
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
import com.particle.game.entity.attribute.metadata.EntityMetaDataModule;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.config.DropConfig;
import com.particle.game.entity.service.config.EquipmentConfig;
import com.particle.game.entity.service.config.MobEntityConfig;
import com.particle.game.entity.service.network.MobEntityPacketBuilder;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.spawn.processor.MobEntitySpawnProcessor;
import com.particle.game.entity.state.EntityStateService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.game.world.physical.EntityColliderService;
import com.particle.game.world.physical.PhysicalService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.EntityType;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.type.EntityTypeData;
import com.particle.model.entity.type.EntityTypeDictionary;
import com.particle.model.inventory.ArmorInventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import com.particle.util.math.GlobalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class MobEntityService implements MobEntityServiceApi {

    private static Logger LOGGER = LoggerFactory.getLogger(MobEntityService.class);

    private static final ECSModuleHandler<EntityNameModule> ENTITY_NAME_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityNameModule.class);

    private static final ECSModuleHandler<EntityMetaDataModule> ENTITY_META_DATA_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMetaDataModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<DeathExperienceModule> DEATH_EXPERIENCE_MODULE_HANDLER = ECSModuleHandler.buildHandler(DeathExperienceModule.class);


    @Inject
    private MobEntitySpawnProcessor mobEntitySpawnProcessor;
    @Inject
    private MobEntityPacketBuilder mobEntityPacketBuilder;

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
        MobEntityConfigService.loadMobEntityConfig();
    }


    @Override
    public MobEntity createEntity(int networkId, Vector3f position) {
        EntityType entityType = EntityType.fromValue(networkId);
        if (entityType == null) {
            return null;
        }

        return this.createEntity(entityType.actorType(), networkId, position);
    }

    @Override
    public MobEntity createEntity(String actorType, Vector3f position) {
        EntityTypeData entityTypeData = EntityTypeDictionary.getMobEntityConfig(actorType);
        if (entityTypeData == null) {
            return null;
        }

        return this.createEntity(actorType, entityTypeData.getType(), position);
    }

    private MobEntity createEntity(String actorType, int networkId, Vector3f position) {
        MobEntity mobEntity = new MobEntity(actorType, networkId);
        mobEntity.init();

        MobEntityConfig mobEntityConfig = MobEntityConfigService.getMobEntityConfig(actorType);

        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(mobEntity);
        transformModule.setPosition(position);
        transformModule.setDirection(0, 0, 0);
        transformModule.setMoveEntityPacketBuilder(this.mobEntityPacketBuilder.getMovePacketBuilder(mobEntity));

        // 设置Entity基础信息
        EntityMetaDataModule entityMetaDataModule = ENTITY_META_DATA_MODULE_HANDLER.bindModule(mobEntity);
        entityMetaDataModule.setLongData(EntityMetadataType.FLAGS, 0);
        entityMetaDataModule.setFloatData(EntityMetadataType.SCALE, 1f);

        ENTITY_NAME_MODULE_HANDLER.bindModule(mobEntity);

        this.healthServiceProxy.initHealthComponent(mobEntity, mobEntityConfig.getHealth());
        this.entityStateService.initEntityStateComponent(mobEntity);

        // 移动相关
        this.movementServiceProxy.enableMovement(mobEntity, mobEntityConfig.getSpeed(), mobEntityConfig.getMaxSpeed());
        this.entityColliderService.bindAABBBindBox(
                mobEntity,
                position,
                new Vector3f(mobEntityConfig.getBindBoxX(), mobEntityConfig.getBindBoxY(), mobEntityConfig.getBindBoxZ()),
                new Vector3f(mobEntityConfig.getBindBoxLengthX(), mobEntityConfig.getBindBoxLengthY(), mobEntityConfig.getBindBoxLengthZ()));
        this.entityColliderService.bindDefaultColliderDetector(mobEntity);
        this.physicalService.initPhysicalEffects(mobEntity, false, !(mobEntityConfig.getGravity() == 0), mobEntityConfig.getGravity());

        // Spawn相关业务
        this.entitySpawnService.enableSpawn(
                mobEntity,
                this.mobEntitySpawnProcessor.getEntitySpawnProcessor(mobEntity),
                this.mobEntityPacketBuilder.getAddPacketBuilder(mobEntity),
                this.mobEntityPacketBuilder.getRemovePacketBuilder(mobEntity)
        );

        // 伤害相关业务
        // TODO: 2019/4/29 优化下，不一定要帮攻击组件
        this.entityAttackService.initEntityAttackComponent(mobEntity, mobEntityConfig.getDamage(), 1000);
        this.entityRemoteAttackService.initEntityRemoteAttackComponent(mobEntity, 3000);
        this.entityAttackedHandleService.initEntityAttackedComponent(mobEntity);

        // 设置死亡经验
        this.setDeathExperience(mobEntity, mobEntityConfig.getExperience());

        // 处理装备
        this.processEntityEquipments(mobEntity, mobEntityConfig);

        // 配置system
        this.ecsComponentManager.filterTickedSystem(mobEntity);

        return mobEntity;
    }

    @Override
    public void setDeathExperience(Entity entity, int amount) {
        DeathExperienceModule experienceComponent = DEATH_EXPERIENCE_MODULE_HANDLER.bindModule(entity);
        experienceComponent.setExperience(amount);
    }

    private void processEntityEquipments(MobEntity entity, MobEntityConfig config) {
        // 基础装备相关
        Map<String, EquipmentConfig> equipments = config.getEquipment();
        if (equipments == null) {
            return;
        }

        for (EquipmentConfig equipmentConfig : equipments.values()) {
            // 随机
            if (GlobalRandom.nextDouble() < equipmentConfig.getProbability()) {
                // 确认背包就绪
                ItemStack equipmentItem = equipmentConfig.getItemStack().clone();
                String position = equipmentConfig.getPosition();

                // 更具不同装备处理装备策略
                if (position.equals("hand")) {
                    // 手上的物品
                    PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(entity, InventoryConstants.CONTAINER_ID_PLAYER);
                    if (playerInventory == null) {
                        playerInventory = new PlayerInventory();
                        this.inventoryManager.bindMultiInventory(entity, playerInventory);
                    }
                    this.playerInventoryAPI.setItem(playerInventory, 0, equipmentItem, false);
                } else {
                    ArmorInventory armorInventory = (ArmorInventory) this.inventoryManager.getInventoryByContainerId(entity, InventoryConstants.CONTAINER_ID_ARMOR);
                    if (armorInventory == null) {
                        armorInventory = new ArmorInventory();
                        this.inventoryManager.bindMultiInventory(entity, armorInventory);
                    }

                    // 处理装备
                    switch (position) {
                        case "helmet":
                            this.playerInventoryAPI.setItem(armorInventory, ArmorInventory.HELMET, equipmentItem);
                            break;
                        case "chestplate":
                            this.playerInventoryAPI.setItem(armorInventory, ArmorInventory.CHESTPLATE, equipmentItem);
                            break;
                        case "leggings":
                            this.playerInventoryAPI.setItem(armorInventory, ArmorInventory.LEGGINGS, equipmentItem);
                            break;
                        case "boots":
                            this.playerInventoryAPI.setItem(armorInventory, ArmorInventory.BOOTS, equipmentItem);
                            break;
                    }
                }

            }
        }
    }

    public void onEntityDeath(MobEntity entity) {
        MobEntityConfig mobEntityConfig = MobEntityConfigService.getMobEntityConfig(entity.getActorType());

        if (mobEntityConfig != null) {
            Map<String, List<DropConfig>> drops = mobEntityConfig.getDrops();

            // 计算掉落物
            List<ItemStack> dropItems = new ArrayList<>();
            for (Map.Entry<String, List<DropConfig>> dropItemConfig : drops.entrySet()) {
                ItemStack itemStack = null;
                for (DropConfig dropConfig : dropItemConfig.getValue()) {
                    if (dropConfig.getProbability() > Math.random()) {
                        if (itemStack == null) {
                            ItemStack dropItemStacks = dropConfig.getItemStack();
                            if (dropItemStacks != null) {
                                itemStack = dropItemStacks.clone();
                                itemStack.setCount(dropConfig.getAmount());
                            }
                        } else {
                            itemStack.setCount(itemStack.getCount() + dropConfig.getAmount());
                        }
                    }
                }
                if (itemStack != null) {
                    dropItems.add(itemStack);
                }
            }

            // 刷新掉落物
            Vector3f spawnPosition = this.positionService.getPosition(entity).add(0, 0.2f, 0);
            for (ItemStack dropItem : dropItems) {
                ItemEntity itemEntity = this.itemEntityService.createEntity(dropItem, spawnPosition);
                this.movementServiceProxy.setMotion(itemEntity, new Vector3f(0, 3f, 0));

                this.entitySpawnService.spawn(entity.getLevel(), itemEntity);
            }

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
}
