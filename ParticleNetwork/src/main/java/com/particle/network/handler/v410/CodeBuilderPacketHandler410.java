package com.particle.network.handler.v410;

import com.particle.model.network.packets.data.CodeBuilderPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class CodeBuilderPacketHandler410 extends AbstractPacketHandler<CodeBuilderPacket> {

    @Override
    protected void doDecode(CodeBuilderPacket dataPacket, int version) {
        dataPacket.setUrl(dataPacket.readString());
        dataPacket.setOpening(dataPacket.readBoolean());
    }

    @Override
    protected void doEncode(CodeBuilderPacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getUrl());
        dataPacket.writeBoolean(dataPacket.isOpening());
    }
}
