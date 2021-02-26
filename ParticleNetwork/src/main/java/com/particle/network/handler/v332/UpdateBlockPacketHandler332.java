package com.particle.network.handler.v332;

import com.particle.model.block.types.BlockPrototype;
import com.particle.model.network.packets.data.UpdateBlockPacket;
import com.particle.network.encoder.NetworkBlockPositionEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class UpdateBlockPacketHandler332 extends AbstractPacketHandler<UpdateBlockPacket> {

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

        int runtimeId = dataPacket.getRuntimeId();
        if (dataPacket.getBlock() != null && dataPacket.getBlock().getType() != BlockPrototype.AIR) {
            runtimeId = version < AbstractPacketHandler.VERSION_1_16 ? dataPacket.getBlock().getRuntimeId(AbstractPacketHandler.VERSION_1_13) : dataPacket.getBlock().getRuntimeId(AbstractPacketHandler.VERSION_1_16);
        }

        dataPacket.writeUnsignedVarInt(runtimeId);
        dataPacket.writeUnsignedVarInt(dataPacket.getFlag());
        dataPacket.writeUnsignedVarInt(dataPacket.getLayer());
    }
}
