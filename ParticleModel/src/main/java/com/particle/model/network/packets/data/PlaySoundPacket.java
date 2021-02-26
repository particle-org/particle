package com.particle.model.network.packets.data;

import com.particle.model.math.Vector3;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class PlaySoundPacket extends DataPacket {

    private String name;

    private Vector3 position;

    private float volume;

    private float pitch;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public int pid() {
        return ProtocolInfo.PLAY_SOUND_PACKET;
    }
}
