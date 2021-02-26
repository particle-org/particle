package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.ServerSettingsResponsePacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ServerSettingsResponsePacketHandler extends AbstractPacketHandler<ServerSettingsResponsePacket> {

    @Override
    protected void doDecode(ServerSettingsResponsePacket dataPacket, int version) {
        dataPacket.setFormId(dataPacket.readUnsignedVarInt());
        dataPacket.setFormUiJson(dataPacket.readString());
    }

    @Override
    protected void doEncode(ServerSettingsResponsePacket dataPacket, int version) {
        dataPacket.writeUnsignedVarInt(dataPacket.getFormId());
        dataPacket.writeString(dataPacket.getFormUiJson());
    }
}
