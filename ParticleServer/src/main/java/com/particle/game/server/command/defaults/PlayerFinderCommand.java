package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.server.Server;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;

@RegisterCommand
@Singleton
@ParentCommand("player")
@CommandPermission(CommandPermissionConstant.CONSOLE)
public class PlayerFinderCommand extends BaseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerFinderCommand.class);

    @Inject
    private Server server;

    @SubCommand("list")
    public void list(CommandSource source) {
        ArrayList<String> playernames = new ArrayList<>();

        for (Player player : server.getAllPlayers()) {
            playernames.add(player.getIdentifiedStr());
        }

        source.sendMessage("在线玩家：" + Arrays.toString(playernames.toArray()));
    }

    @SubCommand("find")
    public void find(CommandSource source, String name) {
        Player player = this.server.getPlayer(name);

        if (player == null) {
            source.sendMessage("None");
        } else {
            source.sendMessage("Here");
        }
    }

}
