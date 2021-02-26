package com.particle.game.entity.service.template;

import com.particle.api.entity.IEntityTemplateCreator;
import com.particle.api.entity.IMobEntityTemplateFactory;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attribute.metadata.EntityMetaDataModule;
import com.particle.game.entity.service.network.MobEntityPacketBuilder;
import com.particle.game.entity.spawn.SpawnModule;
import com.particle.game.entity.spawn.processor.MobEntitySpawnProcessor;
import com.particle.game.entity.state.EntityStateModule;
import com.particle.game.scene.module.BroadcastModule;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MobEntityTemplateFactory implements IMobEntityTemplateFactory {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<EntityMetaDataModule> ENTITY_META_DATA_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMetaDataModule.class);
    private static final ECSModuleHandler<EntityStateModule> ENTITY_STATE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityStateModule.class);
    private static final ECSModuleHandler<BroadcastModule> BROADCAST_MODULE_HANDLER = ECSModuleHandler.buildHandler(BroadcastModule.class);
    private static final ECSModuleHandler<SpawnModule> SPAWN_MODULE_HANDLER = ECSModuleHandler.buildHandler(SpawnModule.class);

    @Inject
    private MobEntityPacketBuilder mobEntityPacketBuilder;
    @Inject
    private MobEntitySpawnProcessor mobEntitySpawnProcessor;

    @Override
    public IEntityTemplateCreator buildTemplateCreator(int type, String actorType) {
        return new MobEntityTemplate(type, actorType, false);
    }

    @Override
    public IEntityTemplateCreator buildTemplateCreator(int type, String actorType, boolean isBaby) {
        return new MobEntityTemplate(type, actorType, isBaby);
    }

    public class MobEntityTemplate implements IEntityTemplateCreator {

        private int type;
        private String actorType;
        private boolean isBaby;

        public MobEntityTemplate(int type, String actorType, boolean isBaby) {
            this.type = type;
            this.actorType = actorType;
            this.isBaby = isBaby;
        }

        @Override
        public Entity getEntity(Vector3f position) {
            MobEntity mobEntity = new MobEntity(actorType, type);
            mobEntity.init();

            TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(mobEntity);
            transformModule.setPosition(position);
            transformModule.setDirection(0, 0, 0);
            transformModule.setMoveEntityPacketBuilder(mobEntityPacketBuilder.getMovePacketBuilder(mobEntity));

            // 设置Entity基础信息
            EntityMetaDataModule entityMetaDataModule = ENTITY_META_DATA_MODULE_HANDLER.bindModule(mobEntity);
            entityMetaDataModule.setLongData(EntityMetadataType.FLAGS, this.isBaby ? (1L << MetadataDataFlag.DATA_FLAG_BABY.value()) : 0);
            entityMetaDataModule.setFloatData(EntityMetadataType.SCALE, 1f);

            ENTITY_STATE_MODULE_HANDLER.bindModule(mobEntity);

            // 订阅相关业务
            BROADCAST_MODULE_HANDLER.bindModule(mobEntity);

            // Spawn相关业务
            SpawnModule spawnModule = SPAWN_MODULE_HANDLER.bindModule(mobEntity);
            spawnModule.setSpawnEntityProcessor(mobEntitySpawnProcessor.getEntitySpawnProcessor(mobEntity));
            spawnModule.setAddEntityPacketBuilder(mobEntityPacketBuilder.getAddPacketBuilder(mobEntity));
            spawnModule.setRemoveEntityPacketBuilder(mobEntityPacketBuilder.getRemovePacketBuilder(mobEntity));

            return mobEntity;
        }
    }
}

