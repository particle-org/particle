package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.player.spawn.PlayerRespawnState;

public class RespawnPacket extends DataPacket {

    private float x;
    private float y;
    private float z;

    private PlayerRespawnState state;
    private long playerRuntimeId;

    @Override
    public int pid() {
        return ProtocolInfo.RESPAWN_PACKET;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public PlayerRespawnState getState() {
        return state;
    }

    public void setState(PlayerRespawnState state) {
        this.state = state;
    }

    public long getPlayerRuntimeId() {
        return playerRuntimeId;
    }

    public void setPlayerRuntimeId(long playerRuntimeId) {
        this.playerRuntimeId = playerRuntimeId;
    }
}
