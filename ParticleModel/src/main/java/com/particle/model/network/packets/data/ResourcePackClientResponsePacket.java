package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class ResourcePackClientResponsePacket extends DataPacket {

    public static final byte CANCEL = 1;

    public static final byte DOWNLOADING = 2;

    public static final byte DOWNLOADING_FINISHED = 3;

    public static final byte RESOURCE_PACK_STACK_FINISHED = 4;

    private byte response;

    private String[] downloadingPackIds;

    @Override
    public int pid() {
        return ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET;
    }


    public byte getResponse() {
        return response;
    }

    public void setResponse(byte response) {
        this.response = response;
    }

    public String[] getDownloadingPackIds() {
        return downloadingPackIds;
    }

    public void setDownloadingPackIds(String[] downloadingPackIds) {
        this.downloadingPackIds = downloadingPackIds;
    }
}
