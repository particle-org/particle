package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class CodeBuilderPacket extends DataPacket {
    private boolean isOpening;
    private String url = "";

    @Override
    public int pid() {
        return ProtocolInfo.CODE_BUILDER_PACKET;
    }

    public boolean isOpening() {
        return isOpening;
    }

    public void setOpening(boolean opening) {
        isOpening = opening;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
