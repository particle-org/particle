package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.PlayerHotbarPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class PlayerHotbarPacketHandler extends AbstractPacketHandler<PlayerHotbarPacket> {

    @Override
    protected void doDecode(PlayerHotbarPacket dataPacket, int version) {
        dataPacket.setSelectedSlot(dataPacket.readUnsignedVarInt());
        dataPacket.setContainerId(dataPacket.readByte());
        dataPacket.setShouldSelectSlot(dataPacket.readBoolean());
    }

    @Override
    protected void doEncode(PlayerHotbarPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarInt(dataPacket.getSelectedSlot());
        dataPacket.writeByte((byte) dataPacket.getContainerId());
        dataPacket.writeBoolean(dataPacket.isShouldSelectSlot());
    }
}
