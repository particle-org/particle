package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class NetworkChunkPublisherUpdatePacket extends DataPacket {

    private int blockX;

    private int blockY;

    private int blockZ;

    private int newRadiusForView;

    @Override
    public int pid() {
        return ProtocolInfo.NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET;
    }

    public int getBlockX() {
        return blockX;
    }

    public void setBlockX(int blockX) {
        this.blockX = blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public void setBlockY(int blockY) {
        this.blockY = blockY;
    }

    public int getBlockZ() {
        return blockZ;
    }

    public void setBlockZ(int blockZ) {
        this.blockZ = blockZ;
    }

    public int getNewRadiusForView() {
        return newRadiusForView;
    }

    public void setNewRadiusForView(int newRadiusForView) {
        this.newRadiusForView = newRadiusForView;
    }
}
