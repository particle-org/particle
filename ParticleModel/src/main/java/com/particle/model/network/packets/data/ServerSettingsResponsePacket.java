package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class ServerSettingsResponsePacket extends DataPacket {

    @Override
    public int pid() {
        return ProtocolInfo.SERVER_SETTINGS_RESPONSE_PACKET;
    }

    private int formId;

    private String formUiJson;

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public String getFormUiJson() {
        return formUiJson;
    }

    public void setFormUiJson(String formUiJson) {
        this.formUiJson = formUiJson;
    }
}
