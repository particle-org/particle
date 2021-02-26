package com.particle.network.handler.v388;

import com.particle.model.network.packets.data.RespawnPacket;
import com.particle.model.player.spawn.PlayerRespawnState;
import com.particle.network.handler.AbstractPacketHandler;

public class RespawnPacketHandler388 extends AbstractPacketHandler<RespawnPacket> {
    @Override
    protected void doDecode(RespawnPacket dataPacket, int version) {
        dataPacket.setX(dataPacket.readLFloat());
        dataPacket.setY(dataPacket.readLFloat());
        dataPacket.setZ(dataPacket.readLFloat());
        dataPacket.setState(PlayerRespawnState.fromValue(dataPacket.readByte()));
        dataPacket.setPlayerRuntimeId(dataPacket.readUnsignedVarLong());
    }

    @Override
    protected void doEncode(RespawnPacket dataPacket, int version) {
        dataPacket.writeLFloat(dataPacket.getX());
        dataPacket.writeLFloat(dataPacket.getY());
        dataPacket.writeLFloat(dataPacket.getZ());
        dataPacket.writeByte(dataPacket.getState().state());
        dataPacket.writeUnsignedVarLong(dataPacket.getPlayerRuntimeId());
    }
}
