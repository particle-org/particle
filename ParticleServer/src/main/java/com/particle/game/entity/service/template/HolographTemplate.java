package com.particle.game.entity.service.template;

import com.particle.api.entity.IEntityTemplateCreator;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attribute.metadata.EntityMetaDataModule;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.service.network.MobEntityPacketBuilder;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.spawn.processor.MobEntitySpawnProcessor;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.model.entity.Entity;
import com.particle.model.entity.EntityType;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HolographTemplate implements IEntityTemplateCreator {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<EntityMetaDataModule> ENTITY_META_DATA_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMetaDataModule.class);


    @Inject
    private MobEntityPacketBuilder mobEntityPacketBuilder;
    @Inject
    private MobEntitySpawnProcessor mobEntitySpawnProcessor;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    @Inject
    private EntitySpawnService entitySpawnService;


    @Inject
    private ECSComponentManager ecsComponentManager;

    @Override
    public Entity getEntity(Vector3f position) {
        MobEntity mobEntity = new MobEntity(EntityType.ARMOR_STAND.actorType(), EntityType.ARMOR_STAND.type());
        mobEntity.init();


        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(mobEntity);
        transformModule.setPosition(position);
        transformModule.setDirection(0, 0, 0);
        transformModule.setMoveEntityPacketBuilder(this.mobEntityPacketBuilder.getMovePacketBuilder(mobEntity));

        // 设置Entity基础信息
        EntityMetaDataModule entityMetaDataModule = ENTITY_META_DATA_MODULE_HANDLER.bindModule(mobEntity);
        entityMetaDataModule.setLongData(EntityMetadataType.FLAGS, 0);
        entityMetaDataModule.setFloatData(EntityMetadataType.SCALE, 0.001f);
        entityMetaDataModule.setStringData(EntityMetadataType.NAMETAG, "");
        entityMetaDataModule.setFloatData(EntityMetadataType.BOUNDING_BOX_WIDTH, 0.001f);
        entityMetaDataModule.setFloatData(EntityMetadataType.BOUNDING_BOX_HEIGHT, 0.001f);
        entityMetaDataModule.setBooleanData(EntityMetadataType.NAMETAG_ALWAYS_SHOW, true);

        // 移动相关
        this.movementServiceProxy.enableMovement(mobEntity, 0, 0);

        // Spawn相关业务
        this.entitySpawnService.enableSpawn(
                mobEntity,
                this.mobEntitySpawnProcessor.getEntitySpawnProcessor(mobEntity),
                this.mobEntityPacketBuilder.getAddPacketBuilder(mobEntity),
                this.mobEntityPacketBuilder.getRemovePacketBuilder(mobEntity)
        );

        // 配置system
        this.ecsComponentManager.filterTickedSystem(mobEntity);

        return mobEntity;
    }
}
