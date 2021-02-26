package com.particle.network.handler.v410;

import com.particle.model.network.packets.data.EmoteListPacket;
import com.particle.network.encoder.UUIDEncoder;
import com.particle.network.handler.AbstractPacketHandler;

import java.util.UUID;


public class EmoteListPacketHandler410 extends AbstractPacketHandler<EmoteListPacket> {

    private UUIDEncoder uuidEncoder = UUIDEncoder.getInstance();

    @Override
    protected void doDecode(EmoteListPacket dataPacket, int version) {
        dataPacket.setRuntimeId(dataPacket.readUnsignedVarLong());
        int size = dataPacket.readUnsignedVarInt();
        for (int i = 0; i < size; i++) {
            dataPacket.getPieceIdList().add(uuidEncoder.decode(dataPacket, version));
        }
    }

    @Override
    protected void doEncode(EmoteListPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getRuntimeId());
        dataPacket.writeUnsignedVarInt(dataPacket.getPieceIdList().size());
        for (UUID uuid : dataPacket.getPieceIdList()) {
            uuidEncoder.encode(dataPacket, uuid, version);
        }
    }
}
