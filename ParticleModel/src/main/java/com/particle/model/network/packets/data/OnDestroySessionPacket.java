package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class OnDestroySessionPacket extends DataPacket {

    private String reason;

    @Override
    public int pid() {
        return ProtocolInfo.ON_DESTROY_SESSION_HEAD;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
