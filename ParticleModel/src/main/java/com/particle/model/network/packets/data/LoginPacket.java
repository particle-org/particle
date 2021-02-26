package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class LoginPacket extends DataPacket {
    private int protocol;
    private String chainData;
    private String playerData;
    private boolean isInProxy;

    @Override
    public int pid() {
        return ProtocolInfo.LOGIN_PACKET;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getChainData() {
        return chainData;
    }

    public void setChainData(String chainData) {
        this.chainData = chainData;
    }

    public String getPlayerData() {
        return playerData;
    }

    public void setPlayerData(String playerData) {
        this.playerData = playerData;
    }

    public boolean isInProxy() {
        return isInProxy;
    }

    public void setInProxy(boolean inProxy) {
        isInProxy = inProxy;
    }

}
