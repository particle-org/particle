package com.particle.network.handler.v388;

import com.particle.model.network.packets.data.ConfirmSkinPacket;
import com.particle.network.encoder.UUIDEncoder;
import com.particle.network.handler.AbstractPacketHandler;

import java.util.List;
import java.util.UUID;

public class ConfirmSkinPacketHandler388 extends AbstractPacketHandler<ConfirmSkinPacket> {

    private UUIDEncoder uuidEncoder = UUIDEncoder.getInstance();

    @Override
    protected void doDecode(ConfirmSkinPacket dataPacket, int version) {
        int length = dataPacket.readUnsignedVarInt();
        for (int i = 0; i < length; i++) {
            dataPacket.readBoolean();
            dataPacket.addPlayerListEntry(this.uuidEncoder.decode(dataPacket, version));
            dataPacket.readString();
        }
    }

    @Override
    protected void doEncode(ConfirmSkinPacket dataPacket, int version) {
        List<UUID> uuids = dataPacket.getUuids();
        dataPacket.writeUnsignedVarInt(uuids.size());
        for (UUID uuid : uuids) {
            dataPacket.writeBoolean(true);
            this.uuidEncoder.encode(dataPacket, uuid, version);
            dataPacket.writeString("");
        }
    }
}
