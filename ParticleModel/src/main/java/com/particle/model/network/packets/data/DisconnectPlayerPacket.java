package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class DisconnectPlayerPacket extends DataPacket {

    private boolean hideDisconnectionScreen = true;

    private String message;

    @Override
    public int pid() {
        return ProtocolInfo.DISCONNECT_PACKET;
    }


    public void setMessage(String message) {
        this.hideDisconnectionScreen = false;
        this.message = message;
    }

    public boolean isHideDisconnectionScreen() {
        return hideDisconnectionScreen;
    }

    public void setHideDisconnectionScreen(boolean hideDisconnectionScreen) {
        this.hideDisconnectionScreen = hideDisconnectionScreen;
    }

    public String getMessage() {
        return message;
    }
}
