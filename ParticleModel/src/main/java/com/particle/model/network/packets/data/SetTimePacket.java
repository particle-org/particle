package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class SetTimePacket extends DataPacket {

    private int time;

    @Override
    public int pid() {
        return ProtocolInfo.SET_TIME_PACKET;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
