package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class ResourcePackChunkRequestPacket extends DataPacket {

    private String id;

    private int chunkIndex;

    @Override
    public int pid() {
        return ProtocolInfo.RESOURCE_PACK_CHUNK_REQUEST_PACKET;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(int chunkIndex) {
        this.chunkIndex = chunkIndex;
    }
}
