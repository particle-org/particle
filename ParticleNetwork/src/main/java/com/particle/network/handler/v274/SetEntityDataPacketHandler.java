package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.SetEntityDataPacket;
import com.particle.network.encoder.EntityMetaDataEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class SetEntityDataPacketHandler extends AbstractPacketHandler<SetEntityDataPacket> {

    private EntityMetaDataEncoder entityMetaDataEncoder = EntityMetaDataEncoder.getInstance();

    @Override
    protected void doDecode(SetEntityDataPacket dataPacket, int version) {
        dataPacket.setEid(dataPacket.readUnsignedVarLong());
        dataPacket.setMetaData(entityMetaDataEncoder.decode(dataPacket, version));
    }

    @Override
    protected void doEncode(SetEntityDataPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getEid());
        entityMetaDataEncoder.encode(dataPacket, dataPacket.getMetaData(), version);
    }
}
