package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class PlayerStatusPacket extends DataPacket {

    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAILED_CLIENT = 1;
    public static final int LOGIN_FAILED_SERVER = 2;
    public static final int PLAYER_SPAWN = 3;
    public static final int LOGIN_FAILED_INVALID_TENANT = 4;
    public static final int LOGIN_FAILED_VANILLA_EDU = 5;
    public static final int LOGIN_FAILED_EDU_VANILLA = 6;

    private int status;

    @Override
    public int pid() {
        return ProtocolInfo.PLAY_STATUS_PACKET;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
