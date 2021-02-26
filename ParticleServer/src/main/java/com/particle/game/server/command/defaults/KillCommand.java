package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.server.Server;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.KILL)
public class KillCommand extends BaseCommand {

    @Inject
    private Server server;

    @Inject
    private HealthServiceProxy healthServiceProxy;

    @SubCommand("kill")
    public void kill(CommandSource source, String playerName) {
        Player player = this.server.getPlayer(playerName);

        if (player == null) {
            source.sendError("玩家不存在");
        } else {
            this.healthServiceProxy.killEntity(player, null);
        }
    }
}
