package com.particle.network.handler.v410;

import com.particle.model.network.packets.data.HurtArmorPacket;
import com.particle.network.handler.AbstractPacketHandler;


public class HurtArmorPacketHandler410 extends AbstractPacketHandler<HurtArmorPacket> {

    @Override
    protected void doDecode(HurtArmorPacket dataPacket, int version) {

    }

    @Override
    protected void doEncode(HurtArmorPacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getCause());
        dataPacket.writeSignedVarInt(dataPacket.getDamage());
    }
}
