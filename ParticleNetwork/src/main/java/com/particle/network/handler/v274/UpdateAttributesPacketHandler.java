package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.UpdateAttributesPacket;
import com.particle.network.encoder.UpdateEntityAttributesEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class UpdateAttributesPacketHandler extends AbstractPacketHandler<UpdateAttributesPacket> {

    private UpdateEntityAttributesEncoder updateEntityAttributesEncoder = UpdateEntityAttributesEncoder.getInstance();

    @Override
    protected void doDecode(UpdateAttributesPacket dataPacket, int version) {
        dataPacket.setEntityId(dataPacket.readUnsignedVarLong());
        dataPacket.setAttributes(updateEntityAttributesEncoder.decode(dataPacket, version));
    }

    @Override
    protected void doEncode(UpdateAttributesPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityId());
        updateEntityAttributesEncoder.encode(dataPacket, dataPacket.getAttributes(), version);
    }
}
