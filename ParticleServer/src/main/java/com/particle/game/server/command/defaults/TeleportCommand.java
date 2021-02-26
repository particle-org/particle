package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.PlayerService;
import com.particle.game.server.Server;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.math.Vector3f;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.TELEPORT)
public class TeleportCommand extends BaseCommand {

    private String errorMsg = "tp [playerName] <x> <y> <z>";

    @Inject
    private PlayerService playerService;

    @Inject
    private Server server;

    @Inject
    private PositionService positionService;

    @SubCommand("tp")
    public void tp(CommandSource source, String x, String y, String z) {
        if (StringUtils.isEmpty(x) || StringUtils.isEmpty(y) || StringUtils.isEmpty(z)) {
            source.sendError(errorMsg);
            return;
        }

        if (!source.isConsole()) {
            Player player = source.getPlayer();
            this.tp(source, player, x, y, z);
        }
    }

    @SubCommand("tp")
    public void tp(CommandSource source, String playerName, String playerTargetName) {
        if (!source.isConsole()) {
            return;
        }

        Player player = this.server.getPlayer(playerName);
        Player playerTarget = this.server.getPlayer(playerTargetName);

        if (player == null || playerTarget == null) {
            return;
        }

        Vector3f position = this.positionService.getPosition(playerTarget);

        this.playerService.teleport(player, position);
    }


    @SubCommand("tp")
    @CommandPermission(CommandPermissionConstant.CONSOLE)
    public void tp(CommandSource source, String playerName, String x, String y, String z) {
        if (StringUtils.isEmpty(x) || StringUtils.isEmpty(y) || StringUtils.isEmpty(z)) {
            source.sendError(errorMsg);
            return;
        }

        Player player = this.server.getPlayer(playerName);
        if (player == null) {
            source.sendError("玩家不存在");
            return;
        }
        this.tp(source, player, x, y, z);
    }


    private void tp(CommandSource source, Player player, String x, String y, String z) {
        Vector3f position = new Vector3f(0, 0, 0);
        try {
            position.setX(Float.parseFloat(x));
            position.setY(Float.parseFloat(y));
            position.setZ(Float.parseFloat(z));
        } catch (NumberFormatException e) {
            source.sendError(errorMsg);
            return;
        }
        playerService.teleport(player, position);
    }
}
