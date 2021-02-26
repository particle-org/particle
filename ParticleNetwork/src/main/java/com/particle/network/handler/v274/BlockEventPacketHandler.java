package com.particle.network.handler.v274;

import com.particle.model.math.Vector3;
import com.particle.model.network.packets.data.BlockEventPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class BlockEventPacketHandler extends AbstractPacketHandler<BlockEventPacket> {

    @Override
    protected void doDecode(BlockEventPacket dataPacket, int version) {
        int x = dataPacket.readSignedVarInt();
        int y = dataPacket.readUnsignedVarInt();
        int z = dataPacket.readSignedVarInt();
        dataPacket.setPosition(new Vector3(x, y, z));
        dataPacket.setEventType(dataPacket.readSignedVarInt());
        dataPacket.setEventValue(dataPacket.readSignedVarInt());
    }

    @Override
    protected void doEncode(BlockEventPacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getPosition().getX());
        dataPacket.writeUnsignedVarInt(dataPacket.getPosition().getY());
        dataPacket.writeSignedVarInt(dataPacket.getPosition().getZ());
        dataPacket.writeSignedVarInt(dataPacket.getEventType());
        dataPacket.writeSignedVarInt(dataPacket.getEventValue());
    }
}
