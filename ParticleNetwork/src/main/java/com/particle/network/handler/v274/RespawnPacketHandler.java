package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.RespawnPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class RespawnPacketHandler extends AbstractPacketHandler<RespawnPacket> {
    @Override
    protected void doDecode(RespawnPacket dataPacket, int version) {
        dataPacket.setX(dataPacket.readLFloat());
        dataPacket.setY(dataPacket.readLFloat());
        dataPacket.setZ(dataPacket.readLFloat());
    }

    @Override
    protected void doEncode(RespawnPacket dataPacket, int version) {
        dataPacket.writeLFloat(dataPacket.getX());
        dataPacket.writeLFloat(dataPacket.getY());
        dataPacket.writeLFloat(dataPacket.getZ());
    }
}
