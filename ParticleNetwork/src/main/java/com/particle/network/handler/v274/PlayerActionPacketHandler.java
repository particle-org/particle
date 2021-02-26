package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.PlayerActionPacket;
import com.particle.model.player.action.PlayerActionsType;
import com.particle.network.encoder.NetworkBlockPositionEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class PlayerActionPacketHandler extends AbstractPacketHandler<PlayerActionPacket> {

    private NetworkBlockPositionEncoder networkBlockPositionEncoder = NetworkBlockPositionEncoder.getInstance();

    @Override
    protected void doDecode(PlayerActionPacket dataPacket, int version) {
        dataPacket.setEntityID(dataPacket.readUnsignedVarLong());
        dataPacket.setAction(PlayerActionsType.fromIntType(dataPacket.readSignedVarInt()));
        dataPacket.setPosition(networkBlockPositionEncoder.decode(dataPacket, version));
        dataPacket.setFace(dataPacket.readSignedVarInt());
    }

    @Override
    protected void doEncode(PlayerActionPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityID());
        dataPacket.writeSignedVarInt(dataPacket.getAction().getType());
        networkBlockPositionEncoder.encode(dataPacket, dataPacket.getPosition(), version);
        dataPacket.writeSignedVarInt(dataPacket.getFace());
    }
}
