package com.particle.game.entity.service.network;

import com.particle.api.entity.function.IAddEntityPacketBuilder;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.item.ItemBindModule;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.AddItemEntityPacket;

import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ItemEntityPacketBuilder extends AliveEntityPacketBuilder {

    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<ItemBindModule> ITEM_BIND_MODULE_HANDLER = ECSModuleHandler.buildHandler(ItemBindModule.class);


    public IAddEntityPacketBuilder getAddPacketBuilder(ItemEntity entity) {
        return new AddPacketBuilder(entity);
    }

    private class AddPacketBuilder implements IAddEntityPacketBuilder {

        private ItemEntity entity;
        private EntityMovementModule entityMovementModule;
        private TransformModule transformModule;

        public AddPacketBuilder(ItemEntity entity) {
            this.entity = entity;
            this.entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public DataPacket[] buildPacket() {
            return new DataPacket[]{this.buildAddPacket()};
        }

        private DataPacket buildAddPacket() {
            AddItemEntityPacket addEntityPacket = new AddItemEntityPacket();

            addEntityPacket.setEntityUniqueId(this.entity.getRuntimeId());
            addEntityPacket.setEntityRuntimeId(this.entity.getRuntimeId());

            addEntityPacket.setItemStack(ITEM_BIND_MODULE_HANDLER.getModule(entity).getItem());

            addEntityPacket.setPosition(this.transformModule.getPosition());

            addEntityPacket.setSpeedX(this.entityMovementModule.getMotionX() / 20);
            addEntityPacket.setSpeedY(this.entityMovementModule.getMotionY() / 20);
            addEntityPacket.setSpeedZ(this.entityMovementModule.getMotionZ() / 20);
            addEntityPacket.setMetadata(new ConcurrentHashMap<>());

            return addEntityPacket;
        }

    }


}
