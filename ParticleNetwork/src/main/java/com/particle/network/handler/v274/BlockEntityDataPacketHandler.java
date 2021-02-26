package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.BlockEntityDataPacket;
import com.particle.network.encoder.NBTTagCompoundEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class BlockEntityDataPacketHandler extends AbstractPacketHandler<BlockEntityDataPacket> {

    private NBTTagCompoundEncoder nbtTagCompoundEncoder = NBTTagCompoundEncoder.getInstance();

    @Override
    protected void doDecode(BlockEntityDataPacket dataPacket, int version) {
        dataPacket.setX(dataPacket.readSignedVarInt());
        dataPacket.setY(dataPacket.readUnsignedVarInt());
        dataPacket.setZ(dataPacket.readSignedVarInt());
        dataPacket.setNbtTagCompound(nbtTagCompoundEncoder.decode(dataPacket, version));
    }

    @Override
    protected void doEncode(BlockEntityDataPacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getX());
        dataPacket.writeUnsignedVarLong(dataPacket.getY());
        dataPacket.writeSignedVarInt(dataPacket.getZ());
        nbtTagCompoundEncoder.encode(dataPacket, dataPacket.getNbtTagCompound(), version);
    }
}
