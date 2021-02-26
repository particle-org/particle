package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.ModalFormResponsePacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ModalFormResponsePacketHandler extends AbstractPacketHandler<ModalFormResponsePacket> {

    @Override
    protected void doDecode(ModalFormResponsePacket dataPacket, int version) {
        dataPacket.setFormId(dataPacket.readUnsignedVarInt());
        dataPacket.setJsonResponse(dataPacket.readString());
    }

    @Override
    protected void doEncode(ModalFormResponsePacket dataPacket, int version) {
        dataPacket.writeUnsignedVarInt(dataPacket.getFormId());
        dataPacket.writeString(dataPacket.getJsonResponse());
    }
}
