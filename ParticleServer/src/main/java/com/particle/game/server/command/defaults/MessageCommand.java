package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.server.Server;
import com.particle.game.ui.TextService;
import com.particle.game.ui.TitleService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@ParentCommand("tmessage")
@CommandPermission(CommandPermissionConstant.TEST)
public class MessageCommand extends BaseCommand {

    @Inject
    private Server server;

    @Inject
    private TitleService titleService;

    @Inject
    private TextService textService;

    @SubCommand("chat")
    public void chat(CommandSource source, String playerName, String message) {
        Player player = this.server.getPlayer(playerName);

        if (player == null) {
            source.sendError("玩家不存在");
            return;
        }

        this.textService.sendChatMessage(player, message);
    }

}
