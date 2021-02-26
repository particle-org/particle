package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.StopSoundPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class StopSoundPacketHandler extends AbstractPacketHandler<StopSoundPacket> {

    @Override
    protected void doDecode(StopSoundPacket dataPacket, int version) {
        dataPacket.setSoundName(dataPacket.readString());
        dataPacket.setStopAllSounds(dataPacket.readBoolean());
    }

    @Override
    protected void doEncode(StopSoundPacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getSoundName());
        dataPacket.writeBoolean(dataPacket.isStopAllSounds());
    }
}
