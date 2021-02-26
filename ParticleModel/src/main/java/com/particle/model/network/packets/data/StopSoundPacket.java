package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class StopSoundPacket extends DataPacket {

    private String soundName;


    private boolean stopAllSounds;

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public boolean isStopAllSounds() {
        return stopAllSounds;
    }

    public void setStopAllSounds(boolean stopAllSounds) {
        this.stopAllSounds = stopAllSounds;
    }

    @Override
    public int pid() {
        return ProtocolInfo.STOP_SOUND_PACKET;
    }
}
