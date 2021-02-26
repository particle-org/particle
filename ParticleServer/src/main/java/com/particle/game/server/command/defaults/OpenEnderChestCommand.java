package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.server.Server;
import com.particle.game.ui.EnderBagService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.TEST)
public class OpenEnderChestCommand extends BaseCommand {

    @Inject
    private EnderBagService enderBagService;

    @Inject
    private Server server;

    @SubCommand("openEnderChest")
    public void test(CommandSource source, String playerName) {
        Player player = this.server.getPlayer(playerName);
        if (player == null) {
            source.sendError("Player not exist!");
            return;
        }

        this.enderBagService.open(player);
    }

}
