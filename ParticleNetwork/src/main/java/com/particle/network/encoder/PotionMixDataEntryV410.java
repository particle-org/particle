package com.particle.network.encoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.PacketBuffer;

public class PotionMixDataEntryV410 {

    public PacketBuffer buffer;

    public PotionMixDataEntryV410(JSONArray data) {
        this.buffer = new PacketBuffer();
        this.buffer.writeUnsignedVarInt(data.size());

        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = data.getJSONObject(i);
            this.buffer.writeSignedVarInt(jsonObject.getInteger("inputId"));
            this.buffer.writeSignedVarInt(jsonObject.getInteger("inputMeta"));
            this.buffer.writeSignedVarInt(jsonObject.getInteger("reagentId"));
            this.buffer.writeSignedVarInt(jsonObject.getInteger("reagentMeta"));
            this.buffer.writeSignedVarInt(jsonObject.getInteger("outputId"));
            this.buffer.writeSignedVarInt(jsonObject.getInteger("outputMeta"));
        }
    }

    public void encode(DataPacket dataPacket) {
        dataPacket.writeBytes(this.buffer.getBuffer());
    }
}
