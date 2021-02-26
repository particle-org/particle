package com.particle.network.handler.v274;

import com.particle.model.block.types.BlockPrototype;
import com.particle.model.network.packets.data.LevelEventPacket;
import com.particle.network.encoder.PositionFEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class LevelEventPacketHandler extends AbstractPacketHandler<LevelEventPacket> {

    private PositionFEncoder positionFEncoder = PositionFEncoder.getInstance();

    @Override
    protected void doDecode(LevelEventPacket dataPacket, int version) {
        dataPacket.setEventType(dataPacket.readSignedVarInt());
        dataPacket.setPosition(positionFEncoder.decode(dataPacket, version));
        dataPacket.setData(dataPacket.readSignedVarInt());
    }

    @Override
    protected void doEncode(LevelEventPacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getEventType());
        positionFEncoder.encode(dataPacket, dataPacket.getPosition(), version);

        int data = dataPacket.getData();
        if (dataPacket.getBlock() != null && dataPacket.getBlock().getType() != BlockPrototype.AIR) {
            data = version < AbstractPacketHandler.VERSION_1_16 ? dataPacket.getBlock().getRuntimeId(AbstractPacketHandler.VERSION_1_13) : dataPacket.getBlock().getRuntimeId(AbstractPacketHandler.VERSION_1_16);
        }
        dataPacket.writeSignedVarInt(data);
    }
}
