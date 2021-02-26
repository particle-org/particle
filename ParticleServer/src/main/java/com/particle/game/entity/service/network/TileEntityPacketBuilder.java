package com.particle.game.entity.service.network;

import com.particle.api.entity.function.IAddEntityPacketBuilder;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.NBTTagCompoundModule;
import com.particle.game.common.modules.TransformModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.BlockEntityDataPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class TileEntityPacketBuilder {

    private static final ECSModuleHandler<NBTTagCompoundModule> NBT_TAG_COMPOUND_MODULE_HANDLER = ECSModuleHandler.buildHandler(NBTTagCompoundModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static Logger LOGGER = LoggerFactory.getLogger(TileEntityPacketBuilder.class);

    public IAddEntityPacketBuilder getAddPacketBuilder(TileEntity entity) {
        return new AddPacketBuilder(entity);
    }

    private class AddPacketBuilder implements IAddEntityPacketBuilder {

        private TileEntity entity;
        private TransformModule transformModule;

        public AddPacketBuilder(TileEntity entity) {
            this.entity = entity;
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public DataPacket[] buildPacket() {
            return new DataPacket[]{this.buildAddPacket()};
        }

        private DataPacket buildAddPacket() {
            BlockEntityDataPacket blockEntityDataPacket = new BlockEntityDataPacket();

            blockEntityDataPacket.setX(transformModule.getFloorX());
            blockEntityDataPacket.setY(transformModule.getFloorY());
            blockEntityDataPacket.setZ(transformModule.getFloorZ());

            blockEntityDataPacket.setNbtTagCompound(this.getViewNbtCompound(entity));

            return blockEntityDataPacket;
        }

        private NBTTagCompound getViewNbtCompound(TileEntity entity) {
            return NBT_TAG_COMPOUND_MODULE_HANDLER.getModule(entity).getNbtTagCompound();
        }

    }
}
