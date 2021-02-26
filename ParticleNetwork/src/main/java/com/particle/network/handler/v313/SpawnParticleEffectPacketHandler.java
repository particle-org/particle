package com.particle.network.handler.v313;

import com.particle.model.network.packets.data.SpawnParticleEffectPacket;
import com.particle.network.encoder.PositionFEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class SpawnParticleEffectPacketHandler extends AbstractPacketHandler<SpawnParticleEffectPacket> {

    private PositionFEncoder positionFEncoder = PositionFEncoder.getInstance();

    @Override
    protected void doDecode(SpawnParticleEffectPacket dataPacket, int version) {
        dataPacket.setDimensionId(dataPacket.readByte());
        dataPacket.setPosition(positionFEncoder.decode(dataPacket, AbstractPacketHandler.VERSION_1_8));
        dataPacket.setEffectName(dataPacket.readString());
    }

    @Override
    protected void doEncode(SpawnParticleEffectPacket dataPacket, int version) {
        dataPacket.writeByte((byte) dataPacket.getDimensionId());
        positionFEncoder.encode(dataPacket, dataPacket.getPosition(), AbstractPacketHandler.VERSION_1_8);
        dataPacket.writeString(dataPacket.getEffectName());
    }
}
