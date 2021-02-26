package com.particle.game.entity.service;

import com.particle.api.entity.NpcServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.attribute.identified.EntityNameModule;
import com.particle.game.entity.attribute.identified.UUIDModule;
import com.particle.game.entity.attribute.metadata.EntityMetaDataModule;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.service.network.NpcPacketBuilder;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.spawn.processor.NpcSpawnProcessor;
import com.particle.game.item.ItemBindModule;
import com.particle.game.scene.module.BroadcastModule;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.game.world.physical.modules.RigibodyModule;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.model.others.NpcEntity;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class NpcService implements NpcServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(NpcService.class);

    private static final ECSModuleHandler<EntityNameModule> ENTITY_NAME_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityNameModule.class);

    private static final ECSModuleHandler<EntityMetaDataModule> ENTITY_META_DATA_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMetaDataModule.class);

    private static final ECSModuleHandler<UUIDModule> UUID_MODULE_HANDLER = ECSModuleHandler.buildHandler(UUIDModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<RigibodyModule> RIGIBODY_MODULE_HANDLER = ECSModuleHandler.buildHandler(RigibodyModule.class);

    private static final ECSModuleHandler<ItemBindModule> ITEM_BIND_MODULE_HANDLER = ECSModuleHandler.buildHandler(ItemBindModule.class);

    private static final ECSModuleHandler<BroadcastModule> BROADCAST_MODULE_HANDLER = ECSModuleHandler.buildHandler(BroadcastModule.class);

    @Inject
    private MovementServiceProxy movementServiceProxy;
    @Inject
    private HealthServiceProxy healthServiceProxy;

    @Inject
    private NpcSpawnProcessor npcSpawnProcessor;
    @Inject
    private NpcPacketBuilder npcPacketBuilder;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private ECSComponentManager ecsComponentManager;

    @Override
    public NpcEntity createEntity(Vector3f position) {
        return this.createEntity(position, new Direction(0, 0, 0));
    }

    @Override
    public NpcEntity createEntity(Vector3f position, Direction direction) {
        NpcEntity npcEntity = new NpcEntity();
        npcEntity.init();

        // 位置相关信息
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(npcEntity);
        transformModule.setPosition(position);
        transformModule.setDirection(direction);
        transformModule.setMoveEntityPacketBuilder(this.npcPacketBuilder.getMovePacketBuilder(npcEntity));

        // 移动相关
        this.movementServiceProxy.enableMovement(npcEntity, 0.1f, 0.2f);
        RIGIBODY_MODULE_HANDLER.bindModule(npcEntity);

        // Spawn相关业务
        this.entitySpawnService.enableSpawn(
                npcEntity,
                this.npcSpawnProcessor.getEntitySpawnProcessor(npcEntity),
                this.npcPacketBuilder.getAddPacketBuilder(npcEntity),
                this.npcPacketBuilder.getRemovePacketBuilder(npcEntity)
        );

        // 订阅相关业务
        BROADCAST_MODULE_HANDLER.bindModule(npcEntity);


        // 设置Entity基础信息
        EntityMetaDataModule entityMetaDataModule = ENTITY_META_DATA_MODULE_HANDLER.bindModule(npcEntity);
        entityMetaDataModule.setLongData(EntityMetadataType.FLAGS, 0);
        entityMetaDataModule.setFloatData(EntityMetadataType.SCALE, 1f);

        this.healthServiceProxy.initHealthComponent(npcEntity, 20);

        // UUID相关业务
        UUIDModule uuidModule = UUID_MODULE_HANDLER.bindModule(npcEntity);
        uuidModule.setUuid(UUID.randomUUID());

        ENTITY_NAME_MODULE_HANDLER.bindModule(npcEntity);

        ITEM_BIND_MODULE_HANDLER.bindModule(npcEntity);

        //配置system
        ecsComponentManager.filterTickedSystem(npcEntity);

        return npcEntity;
    }


    @Override
    public UUID getNpcUuid(NpcEntity npcEntity) {
        UUIDModule module = UUID_MODULE_HANDLER.getModule(npcEntity);
        if (module != null) {
            return module.getUuid();
        }

        return null;
    }
}

