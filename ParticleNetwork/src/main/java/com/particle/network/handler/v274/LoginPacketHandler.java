package com.particle.network.handler.v274;

import com.particle.model.network.packets.PacketBuffer;
import com.particle.model.network.packets.data.LoginPacket;
import com.particle.network.handler.AbstractPacketHandler;

import java.nio.charset.StandardCharsets;

public class LoginPacketHandler extends AbstractPacketHandler<LoginPacket> {

    @Override
    protected void doDecode(LoginPacket dataPacket, int version) {
        int protocol = dataPacket.readShort();
        if (protocol == 0) {
            protocol = dataPacket.readShort();
        }
        // proxy version is above 1000
        if (protocol < 1000) {
            dataPacket.setInProxy(false);
        } else {
            dataPacket.setInProxy(true);
            protocol -= 1000;
        }

        dataPacket.setProtocol(protocol);

        PacketBuffer packetBuffer = new PacketBuffer(dataPacket.readSlice(dataPacket.readUnsignedVarInt()));

        byte[] chainDataBytes = new byte[packetBuffer.readLInt()];
        packetBuffer.readBytes(chainDataBytes);
        dataPacket.setChainData(new String(chainDataBytes, StandardCharsets.UTF_8));

        byte[] playerDataByte = new byte[packetBuffer.readLInt()];
        packetBuffer.readBytes(playerDataByte);
        dataPacket.setPlayerData(new String(playerDataByte, StandardCharsets.UTF_8));
    }

    @Override
    protected void doEncode(LoginPacket dataPacket, int version) {
        dataPacket.writeInt(dataPacket.getProtocol());

        byte[] chainDataBytes = dataPacket.getChainData().getBytes(StandardCharsets.UTF_8);
        byte[] playerDataByte = dataPacket.getPlayerData().getBytes(StandardCharsets.UTF_8);

        PacketBuffer packetBuffer = new PacketBuffer();
        packetBuffer.writeLInt(chainDataBytes.length);
        packetBuffer.writeBytes(chainDataBytes);
        packetBuffer.writeLInt(playerDataByte.length);
        packetBuffer.writeBytes(playerDataByte);

        dataPacket.writeUnsignedVarInt(packetBuffer.readableLength());
        dataPacket.append(packetBuffer.getBuffer());
    }

    protected void doDecodeHead(LoginPacket dataPacket, int version) {
        // 读包头
        dataPacket.readUnsignedVarInt();
        int readerIndex = dataPacket.getReadIndex();

        // 获取版本
        int incomingVersion = dataPacket.readInt();
        if (incomingVersion == 0) {
            incomingVersion = dataPacket.readShort();
        }
        // 去除代理版本
        incomingVersion = incomingVersion % 1000;
        dataPacket.setReadIndex(readerIndex);

        if (incomingVersion < VERSION_1_6) {
            dataPacket.readShort();
        }
    }
}
