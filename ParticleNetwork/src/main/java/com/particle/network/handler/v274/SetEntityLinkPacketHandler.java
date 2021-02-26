package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.SetEntityLinkPacket;
import com.particle.network.encoder.EntityLinkEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class SetEntityLinkPacketHandler extends AbstractPacketHandler<SetEntityLinkPacket> {

    private EntityLinkEncoder entityLinkEncoder = EntityLinkEncoder.getInstance();

    @Override
    protected void doDecode(SetEntityLinkPacket dataPacket, int version) {
        dataPacket.setEntityLink(this.entityLinkEncoder.decode(dataPacket, version));
    }

    @Override
    protected void doEncode(SetEntityLinkPacket dataPacket, int version) {
        this.entityLinkEncoder.encode(dataPacket, dataPacket.getEntityLink(), version);
    }
}
