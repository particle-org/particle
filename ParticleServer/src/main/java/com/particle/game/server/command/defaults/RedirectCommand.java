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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.CONSOLE)
public class RedirectCommand extends BaseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectCommand.class);

    @Inject
    private Server server;

    @Inject
    private PlayerService playerService;

    @SubCommand("redirect")
    public void redirect(CommandSource source, String addr, String sport) {
        LOGGER.info("Redirect to {}:{}", addr, sport);

        short port = Short.parseShort(sport);
        for (Player player : this.server.getAllPlayers()) {
            this.playerService.transfer(player, addr, port);
        }
    }

}
