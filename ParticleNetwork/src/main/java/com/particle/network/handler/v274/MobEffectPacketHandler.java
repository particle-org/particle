package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.MobEffectPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class MobEffectPacketHandler extends AbstractPacketHandler<MobEffectPacket> {
    @Override
    protected void doDecode(MobEffectPacket dataPacket, int version) {
        dataPacket.setEntityId(dataPacket.readUnsignedVarLong());
        dataPacket.setEventId(dataPacket.readByte());
        dataPacket.setEffectId(dataPacket.readSignedVarInt());
        dataPacket.setAmplifier(dataPacket.readSignedVarInt());
        dataPacket.setParticles(dataPacket.readBoolean());
        dataPacket.setDuration(dataPacket.readSignedVarInt());
    }

    @Override
    protected void doEncode(MobEffectPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityId());
        dataPacket.writeByte((byte) dataPacket.getEventId());
        dataPacket.writeSignedVarInt(dataPacket.getEffectId());
        dataPacket.writeSignedVarInt(dataPacket.getAmplifier());
        dataPacket.writeBoolean(dataPacket.isParticles());
        dataPacket.writeSignedVarInt(dataPacket.getDuration());
    }
}
