package com.particle.network.handler.v274;

import com.particle.model.command.CommandOriginData;
import com.particle.model.command.CommandType;
import com.particle.model.network.packets.data.CommandRequestPacket;
import com.particle.network.handler.AbstractPacketHandler;
import com.particle.util.UuidUtils;

import java.util.UUID;

public class CommandRequestPacketHandler extends AbstractPacketHandler<CommandRequestPacket> {

    @Override
    protected void doDecode(CommandRequestPacket dataPacket, int version) {
        dataPacket.setCommand(dataPacket.readString());
        CommandOriginData commandOriginData = new CommandOriginData();
        CommandType originType = CommandType.values()[dataPacket.readUnsignedVarInt()];
        byte[] uuidByte = new byte[16];
        dataPacket.readBytes(uuidByte);
        UUID uuid = UuidUtils.toUuid(uuidByte);
        String requestId = dataPacket.readString();
        long playerId = 0;
        if (originType == CommandType.DevConsole || originType == CommandType.Test) {
            playerId = dataPacket.readSignedVarLong().longValue();
        }
        commandOriginData.setCommandType(originType);
        commandOriginData.setRequestId(requestId);
        commandOriginData.setUuid(uuid);
        dataPacket.setCommandOriginData(commandOriginData);
        // TODO
        dataPacket.setInternal(false);
    }

    @Override
    protected void doEncode(CommandRequestPacket dataPacket, int version) {

    }
}
