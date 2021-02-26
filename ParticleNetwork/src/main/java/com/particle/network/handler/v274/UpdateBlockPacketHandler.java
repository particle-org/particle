package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.UpdateBlockPacket;
import com.particle.network.encoder.NetworkBlockPositionEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class UpdateBlockPacketHandler extends AbstractPacketHandler<UpdateBlockPacket> {

    private NetworkBlockPositionEncoder networkBlockPositionEncoder = NetworkBlockPositionEncoder.getInstance();

    @Override
    protected void doDecode(UpdateBlockPacket dataPacket, int version) {
        dataPacket.setVector3(networkBlockPositionEncoder.decode(dataPacket, version));
        dataPacket.setRuntimeId(dataPacket.readUnsignedVarInt());
        dataPacket.setFlag(dataPacket.readUnsignedVarInt());
        dataPacket.setLayer(dataPacket.readUnsignedVarInt());
    }

    @Override
    protected void doEncode(UpdateBlockPacket dataPacket, int version) {
        networkBlockPositionEncoder.encode(dataPacket, dataPacket.getVector3(), version);
        dataPacket.writeUnsignedVarInt(dataPacket.getRuntimeId());
        dataPacket.writeUnsignedVarInt(dataPacket.getFlag());
        dataPacket.writeUnsignedVarInt(dataPacket.getLayer());
    }
}
