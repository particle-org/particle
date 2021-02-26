package com.particle.network.handler.v274;

import com.particle.model.math.Vector3;
import com.particle.model.network.packets.data.PlaySoundPacket;
import com.particle.network.encoder.NetworkBlockPositionEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class PlaySoundPacketHandler extends AbstractPacketHandler<PlaySoundPacket> {

    private NetworkBlockPositionEncoder networkBlockPositionEncoder = NetworkBlockPositionEncoder.getInstance();

    @Override
    protected void doDecode(PlaySoundPacket dataPacket, int version) {
        dataPacket.setName(dataPacket.readString());
        Vector3 position = this.networkBlockPositionEncoder.decode(dataPacket, version);
        position = position.divide(8);
        dataPacket.setPosition(position);
        dataPacket.setVolume(dataPacket.readLFloat());
        dataPacket.setPitch(dataPacket.readLFloat());
    }

    @Override
    protected void doEncode(PlaySoundPacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getName());
        Vector3 position = dataPacket.getPosition();
        position = position.multiply(8);
        this.networkBlockPositionEncoder.encode(dataPacket, position, version);
        dataPacket.writeLFloat(dataPacket.getVolume());
        dataPacket.writeLFloat(dataPacket.getPitch());
    }
}
