package com.particle.game.entity.service.network;

import com.particle.api.entity.function.IAddEntityPacketBuilder;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attribute.metadata.EntityMetaDataModule;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.model.entity.Entity;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.AddEntityPacket;

import javax.inject.Singleton;

@Singleton
public class ProjectileEntityPacketBuilder extends AliveEntityPacketBuilder {

    private static final ECSModuleHandler<EntityMetaDataModule> ENTITY_META_DATA_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMetaDataModule.class);

    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    public IAddEntityPacketBuilder getAddPacketBuilder(Entity entity) {
        return new AddPacketBuilder(entity);
    }

    private class AddPacketBuilder implements IAddEntityPacketBuilder {

        private Entity entity;
        private EntityMovementModule entityMovementModule;
        private EntityMetaDataModule entityMetaDataModule;
        private TransformModule transformModule;

        public AddPacketBuilder(Entity entity) {
            this.entity = entity;
            this.entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
            this.entityMetaDataModule = ENTITY_META_DATA_MODULE_HANDLER.getModule(entity);
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public DataPacket[] buildPacket() {
            return new DataPacket[]{this.buildAddPacket()};
        }

        private DataPacket buildAddPacket() {
            AddEntityPacket addEntityPacket = new AddEntityPacket();

            addEntityPacket.setActorType(this.entity.getActorType());

            addEntityPacket.setEntityUniqueId(this.entity.getRuntimeId());
            addEntityPacket.setEntityRuntimeId(this.entity.getRuntimeId());

            addEntityPacket.setPosition(this.transformModule.getPosition());
            addEntityPacket.setDirection(this.transformModule.getDirection());

            addEntityPacket.setSpeedX(this.entityMovementModule.getMotionX());
            addEntityPacket.setSpeedY(this.entityMovementModule.getMotionY());
            addEntityPacket.setSpeedZ(this.entityMovementModule.getMotionZ());

            addEntityPacket.setMetadata(this.entityMetaDataModule.getEntityMetaData());

            return addEntityPacket;
        }

    }

}
