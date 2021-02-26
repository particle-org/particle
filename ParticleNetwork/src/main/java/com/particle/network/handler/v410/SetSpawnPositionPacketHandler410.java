package com.particle.network.handler.v410;

import com.particle.model.network.packets.data.SetSpawnPositionPacket;
import com.particle.network.encoder.NetworkBlockPositionEncoder;
import com.particle.network.handler.AbstractPacketHandler;


public class SetSpawnPositionPacketHandler410 extends AbstractPacketHandler<SetSpawnPositionPacket> {

    private NetworkBlockPositionEncoder networkBlockPositionEncoder = NetworkBlockPositionEncoder.getInstance();

    @Override
    protected void doDecode(SetSpawnPositionPacket dataPacket, int version) {
        dataPacket.setSpawnType(dataPacket.readSignedVarInt());
        dataPacket.setBlockVector3(networkBlockPositionEncoder.decode(dataPacket, version));
        dataPacket.setDimension(dataPacket.readSignedVarInt());
        networkBlockPositionEncoder.decode(dataPacket, version);
    }

    @Override
    protected void doEncode(SetSpawnPositionPacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getSpawnType());
        networkBlockPositionEncoder.encode(dataPacket, dataPacket.getBlockVector3(), version);
        dataPacket.writeSignedVarInt(dataPacket.getDimension());
        networkBlockPositionEncoder.encode(dataPacket, dataPacket.getBlockVector3(), version);
    }
}
