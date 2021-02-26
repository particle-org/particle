package com.particle.network.encoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.PacketBuffer;

public class PotionMixDataEntryV388 {

    public PacketBuffer buffer;

    public PotionMixDataEntryV388(JSONArray data) {
        this.buffer = new PacketBuffer();
        this.buffer.writeUnsignedVarInt(data.size());

        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = data.getJSONObject(i);
            this.buffer.writeSignedVarInt(jsonObject.getInteger("input"));
            this.buffer.writeSignedVarInt(jsonObject.getInteger("reagent"));
            this.buffer.writeSignedVarInt(jsonObject.getInteger("output"));
        }
    }

    public void encode(DataPacket dataPacket) {
        dataPacket.writeBytes(this.buffer.getBuffer());
    }
}
