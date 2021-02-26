package com.particle.network.handler.v354;

import com.particle.model.network.packets.data.AddItemEntityPacket;
import com.particle.network.encoder.EntityMetaDataEncoderV354;
import com.particle.network.encoder.ItemStackEncoder;
import com.particle.network.encoder.PositionFEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class AddItemEntityPacketHandler354 extends AbstractPacketHandler<AddItemEntityPacket> {

    private ItemStackEncoder itemStackEncoder = ItemStackEncoder.getInstance();

    private EntityMetaDataEncoderV354 entityMetaDataEncoder = EntityMetaDataEncoderV354.getInstance();

    private PositionFEncoder positionFEncoder = PositionFEncoder.getInstance();

    @Override
    protected void doDecode(AddItemEntityPacket dataPacket, int version) {
        dataPacket.setEntityUniqueId(dataPacket.readSignedVarLong().longValue());
        dataPacket.setEntityRuntimeId(dataPacket.readUnsignedVarLong());

        dataPacket.setItemStack(itemStackEncoder.decode(dataPacket, version));

        dataPacket.setPosition(positionFEncoder.decode(dataPacket, version));
        dataPacket.setSpeedX(dataPacket.readLFloat());
        dataPacket.setSpeedY(dataPacket.readLFloat());
        dataPacket.setSpeedZ(dataPacket.readLFloat());

        dataPacket.setMetadata(entityMetaDataEncoder.decode(dataPacket, version));
        dataPacket.setFromFishing(dataPacket.readBoolean());
    }

    @Override
    protected void doEncode(AddItemEntityPacket dataPacket, int version) {
        dataPacket.writeSignedVarLong(dataPacket.getEntityUniqueId());
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityRuntimeId());

        itemStackEncoder.encode(dataPacket, dataPacket.getItemStack(), version);

        positionFEncoder.encode(dataPacket, dataPacket.getPosition(), version);
        dataPacket.writeLFloat(dataPacket.getSpeedX());
        dataPacket.writeLFloat(dataPacket.getSpeedY());
        dataPacket.writeLFloat(dataPacket.getSpeedZ());

        entityMetaDataEncoder.encode(dataPacket, dataPacket.getMetadata(), version);
        dataPacket.writeBoolean(dataPacket.isFromFishing());
    }
}
