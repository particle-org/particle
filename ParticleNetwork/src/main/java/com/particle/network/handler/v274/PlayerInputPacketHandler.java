package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.PlayerInputPacket;
import com.particle.network.encoder.Vector2FEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class PlayerInputPacketHandler extends AbstractPacketHandler<PlayerInputPacket> {

    private Vector2FEncoder vector2FEncoder = Vector2FEncoder.getInstance();

    @Override
    protected void doDecode(PlayerInputPacket dataPacket, int version) {
        dataPacket.setMoveVector(this.vector2FEncoder.decode(dataPacket, version));
        dataPacket.setJumping(dataPacket.readBoolean());
        dataPacket.setSneaking(dataPacket.readBoolean());
    }

    @Override
    protected void doEncode(PlayerInputPacket dataPacket, int version) {
        this.vector2FEncoder.encode(dataPacket, dataPacket.getMoveVector(), version);
        dataPacket.writeBoolean(dataPacket.isJumping());
        dataPacket.writeBoolean(dataPacket.isSneaking());
    }
}
