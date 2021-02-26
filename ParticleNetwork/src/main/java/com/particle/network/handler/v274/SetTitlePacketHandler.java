package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.SetTitlePacket;
import com.particle.network.handler.AbstractPacketHandler;

public class SetTitlePacketHandler extends AbstractPacketHandler<SetTitlePacket> {

    @Override
    protected void doDecode(SetTitlePacket dataPacket, int version) {
        dataPacket.setTitleType(dataPacket.readSignedVarInt());
        dataPacket.setTitleText(dataPacket.readString());
        dataPacket.setFadeInTime(dataPacket.readSignedVarInt());
        dataPacket.setStayTime(dataPacket.readSignedVarInt());
        dataPacket.setFadeOutTime(dataPacket.readSignedVarInt());
    }

    @Override
    protected void doEncode(SetTitlePacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getTitleType());
        dataPacket.writeString(dataPacket.getTitleText());
        dataPacket.writeSignedVarInt(dataPacket.getFadeInTime());
        dataPacket.writeSignedVarInt(dataPacket.getStayTime());
        dataPacket.writeSignedVarInt(dataPacket.getFadeOutTime());
    }
}
