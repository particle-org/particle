package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.player.PlayerService;
import com.particle.game.server.Server;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.TEST)
public class TransferCommand extends BaseCommand {

    @Inject
    private Server server;

    @Inject
    private PlayerService playerService;

    private Random random = new Random();

    @SubCommand("transfer")
    public void transfer(CommandSource source, String playerName, String ip, String port) {
        Player player = this.server.getPlayer(playerName);
        String[] addresses = ip.split(",");

        String address = addresses[random.nextInt(addresses.length)];

        if (player == null) {
            source.sendError("Player not exist");
        } else {
            this.playerService.transfer(player, address, Short.parseShort(port));
        }
    }

    @SubCommand("transfer")
    public void transfer(CommandSource source, String playerName, String ipList) {
        Player player = this.server.getPlayer(playerName);

        if (player == null) {
            source.sendError("Player not exist");
        } else {
            String[] addresses = ipList.split(",");

            String address = addresses[random.nextInt(addresses.length)];

            String[] addressPair = address.split(":");
            this.playerService.transfer(player, addressPair[0], Short.parseShort(addressPair[1]));

        }
    }
}
