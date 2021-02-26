package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.ModalFormRequestPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ModalFormRequestPacketHandler extends AbstractPacketHandler<ModalFormRequestPacket> {

    @Override
    protected void doDecode(ModalFormRequestPacket dataPacket, int version) {
        dataPacket.setFormId(dataPacket.readUnsignedVarInt());
        dataPacket.setFormUiJson(dataPacket.readString());
    }

    @Override
    protected void doEncode(ModalFormRequestPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarInt(dataPacket.getFormId());
        dataPacket.writeString(dataPacket.getFormUiJson());
    }
}
