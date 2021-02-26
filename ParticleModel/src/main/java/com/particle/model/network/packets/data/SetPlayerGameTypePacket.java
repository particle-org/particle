package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class SetPlayerGameTypePacket extends DataPacket {

    @Override
    public int pid() {
        return ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET;
    }

    private int gameMode;

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public String toString() {
        return "SetPlayerGameTypePacket{" +
                "gameMode=" + gameMode +
                '}';
    }
}
