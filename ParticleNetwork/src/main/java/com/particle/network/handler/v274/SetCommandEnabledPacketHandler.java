package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.SetCommandEnabledPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class SetCommandEnabledPacketHandler extends AbstractPacketHandler<SetCommandEnabledPacket> {

    @Override
    protected void doDecode(SetCommandEnabledPacket dataPacket, int version) {
    }

    @Override
    protected void doEncode(SetCommandEnabledPacket dataPacket, int version) {
        dataPacket.writeBoolean(dataPacket.isEnabled());
    }
}
