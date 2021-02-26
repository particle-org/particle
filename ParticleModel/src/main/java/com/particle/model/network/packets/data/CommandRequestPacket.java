package com.particle.model.network.packets.data;

import com.particle.model.command.CommandOriginData;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class CommandRequestPacket extends DataPacket {

    private String command;

    private CommandOriginData commandOriginData;

    private boolean isInternal;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public CommandOriginData getCommandOriginData() {
        return commandOriginData;
    }

    public void setCommandOriginData(CommandOriginData commandOriginData) {
        this.commandOriginData = commandOriginData;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void setInternal(boolean internal) {
        isInternal = internal;
    }

    @Override
    public int pid() {
        return ProtocolInfo.COMMAND_REQUEST_PACKET;
    }
}
