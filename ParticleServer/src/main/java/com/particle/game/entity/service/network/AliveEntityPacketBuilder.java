package com.particle.game.entity.service.network;

import com.particle.api.entity.function.IRemoveEntityPacketBuilder;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.TransformModule;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.position.IMoveEntityPacketBuilder;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.MoveEntityPacket;
import com.particle.model.network.packets.data.RemoveEntityPacket;

public abstract class AliveEntityPacketBuilder {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    public IRemoveEntityPacketBuilder getRemovePacketBuilder(Entity entity) {
        return new RemovePacketBuilder(entity);
    }

    public IMoveEntityPacketBuilder getMovePacketBuilder(Entity entity) {
        return new MovePacketBuilder(entity);
    }

    private class RemovePacketBuilder implements IRemoveEntityPacketBuilder {

        private long entityId;

        public RemovePacketBuilder(Entity entity) {
            this.entityId = entity.getRuntimeId();
        }

        @Override
        public DataPacket[] buildPacket() {
            return new DataPacket[]{this.buildRemovePacket()};
        }

        private DataPacket buildRemovePacket() {
            RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
            removeEntityPacket.setEntityUniqueId(this.entityId);

            return removeEntityPacket;
        }

    }

    private class MovePacketBuilder implements IMoveEntityPacketBuilder {

        private long entityId;
        private TransformModule transformModule;

        public MovePacketBuilder(Entity entity) {
            this.entityId = entity.getRuntimeId();
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public DataPacket build() {
            MoveEntityPacket moveEntityPacket = new MoveEntityPacket();
            moveEntityPacket.setEntityId(this.entityId);
            moveEntityPacket.setVector3f(this.transformModule.getPosition());
            moveEntityPacket.setDirection(this.transformModule.getDirection());
            moveEntityPacket.setOnGround(this.transformModule.isOnGround());
            moveEntityPacket.setTeleport(false);

            return moveEntityPacket;
        }
    }

}
