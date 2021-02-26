package com.particle.network.handler.v354;

import com.particle.model.network.packets.data.SetEntityDataPacket;
import com.particle.network.encoder.EntityMetaDataEncoderV354;
import com.particle.network.handler.AbstractPacketHandler;

public class SetEntityDataPacketHandler354 extends AbstractPacketHandler<SetEntityDataPacket> {

    private EntityMetaDataEncoderV354 entityMetaDataEncoder = EntityMetaDataEncoderV354.getInstance();

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
