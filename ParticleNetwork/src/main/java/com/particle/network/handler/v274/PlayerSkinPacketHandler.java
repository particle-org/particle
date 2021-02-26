package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.PlayerSkinPacket;
import com.particle.network.encoder.UUIDEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class PlayerSkinPacketHandler extends AbstractPacketHandler<PlayerSkinPacket> {

    private UUIDEncoder uuidEncoder = UUIDEncoder.getInstance();

    @Override
    protected void doDecode(PlayerSkinPacket dataPacket, int version) {
        dataPacket.setPlayerUUID(uuidEncoder.decode(dataPacket, version));
        dataPacket.setSkinId(dataPacket.readString());
        dataPacket.setSkinName(dataPacket.readString());
        dataPacket.setOldSkinName(dataPacket.readString());
        dataPacket.setSkinData(dataPacket.readString());
        dataPacket.setCapeData(dataPacket.readString());
        dataPacket.setGeometryName(dataPacket.readString());
        dataPacket.setGeometryData(dataPacket.readString());
        dataPacket.setPremium(dataPacket.readBoolean());
    }

    @Override
    protected void doEncode(PlayerSkinPacket dataPacket, int version) {
        uuidEncoder.encode(dataPacket, dataPacket.getPlayerUUID(), version);
        dataPacket.writeString(dataPacket.getSkinId());
        dataPacket.writeString(dataPacket.getSkinName());
        dataPacket.writeString(dataPacket.getOldSkinName());
        dataPacket.writeString(dataPacket.getSkinData());
        dataPacket.writeString(dataPacket.getCapeData());
        dataPacket.writeString(dataPacket.getGeometryName());
        dataPacket.writeString(dataPacket.getGeometryData());
        dataPacket.writeBoolean(dataPacket.isPremium());
    }
}
