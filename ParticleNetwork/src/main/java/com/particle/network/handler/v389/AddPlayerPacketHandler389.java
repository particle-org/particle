package com.particle.network.handler.v389;

import com.particle.model.network.packets.data.AddPlayerPacket;
import com.particle.network.handler.v361.AddPlayerPacketHandler361;

public class AddPlayerPacketHandler389 extends AddPlayerPacketHandler361 {

    @Override
    protected void doDecode(AddPlayerPacket dataPacket, int version) {
        super.doDecode(dataPacket, version);
        // 设备Id
        int platformId = dataPacket.readLInt();
        dataPacket.setBuildPlatformId(platformId);

    }

    @Override
    protected void doEncode(AddPlayerPacket dataPacket, int version) {
        super.doEncode(dataPacket, version);
        dataPacket.writeLInt(dataPacket.getBuildPlatformId());
    }
}
