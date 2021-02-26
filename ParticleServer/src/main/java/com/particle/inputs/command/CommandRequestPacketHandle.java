package com.particle.inputs.command;

import com.particle.api.command.CommandSource;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.server.command.CommandManager;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.CommandRequestPacket;
import com.particle.model.player.Player;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CommandRequestPacketHandle extends PlayerPacketHandle<CommandRequestPacket> {

    private static final Logger logger = LoggerFactory.getLogger(CommandRequestPacketHandle.class);

    @Inject
    private CommandManager commandManager;

    @Inject
    private HealthServiceProxy healthServiceProxy;


    @Override
    protected void handlePacket(Player player, CommandRequestPacket packet) {
        if (!player.isSpawned() || !healthServiceProxy.isAlive(player)) {
            return;
        }
        String command = packet.getCommand();
        if (StringUtils.isEmpty(command)) {
            return;
        }
        if (command.startsWith("/")) {
            command = command.substring(1);
            CommandSource commandSource = commandManager.getPlayerCommandSource(player.getRuntimeId());
            if (commandSource == null) {
                logger.error("cant not get commandSource! source player ：" + player.getIdentifiedStr());
                return;
            }
            this.commandManager.dispatchCommand(commandSource, command);
            return;
        }
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.COMMAND_REQUEST_PACKET;
    }
}
