package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.DisconnectPlayerPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class DisconnectPlayerPacketHandler extends AbstractPacketHandler<DisconnectPlayerPacket> {

    @Override
    protected void doDecode(DisconnectPlayerPacket dataPacket, int version) {
        dataPacket.setHideDisconnectionScreen(dataPacket.readBoolean());
        if (!dataPacket.isHideDisconnectionScreen()) {
            dataPacket.setMessage(dataPacket.readString());
        }
    }

    @Override
    protected void doEncode(DisconnectPlayerPacket dataPacket, int version) {
        dataPacket.writeBoolean(dataPacket.isHideDisconnectionScreen());
        if (!dataPacket.isHideDisconnectionScreen()) {
            dataPacket.writeString(dataPacket.getMessage());
        }
    }
}
