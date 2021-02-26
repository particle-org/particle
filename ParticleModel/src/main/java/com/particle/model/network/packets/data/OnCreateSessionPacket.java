package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.net.InetSocketAddress;

public class OnCreateSessionPacket extends DataPacket {

    private InetSocketAddress clientAddress;

    private long clientId;

    @Override
    public int pid() {
        return ProtocolInfo.ON_CREATE_SESSION_HEAD;
    }

    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(InetSocketAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
