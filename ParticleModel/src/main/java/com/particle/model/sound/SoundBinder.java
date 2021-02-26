package com.particle.model.sound;

import com.particle.model.network.packets.DataPacket;

public class SoundBinder {

    private DataPacket soundPacket;

    private int delay = 0;

    SoundBinder(DataPacket soundPacket, int delay) {
        this.soundPacket = soundPacket;
        this.delay = delay;
    }

    public DataPacket getSoundPacket() {
        return soundPacket;
    }

    public int getDelay() {
        return delay;
    }
}
