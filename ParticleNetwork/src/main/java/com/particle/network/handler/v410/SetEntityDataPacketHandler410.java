package com.particle.network.handler.v410;

import com.particle.model.network.packets.data.SetEntityDataPacket;
import com.particle.network.encoder.EntityMetaDataEncoderV361;
import com.particle.network.handler.AbstractPacketHandler;

public class SetEntityDataPacketHandler410 extends AbstractPacketHandler<SetEntityDataPacket> {

    private EntityMetaDataEncoderV361 entityMetaDataEncoder = EntityMetaDataEncoderV361.getInstance();

    @Override
    protected void doDecode(SetEntityDataPacket dataPacket, int version) {
        dataPacket.setEid(dataPacket.readUnsignedVarLong());
        dataPacket.setMetaData(entityMetaDataEncoder.decode(dataPacket, version));
    }

    @Override
    protected void doEncode(SetEntityDataPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getEid());
        entityMetaDataEncoder.encode(dataPacket, dataPacket.getMetaData(), version);
        // only for netease-version
        if (isChsVersion) {
            dataPacket.writeSignedVarLong(dataPacket.getTick());
        }
    }
}
