package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.player.PlayerService;
import com.particle.game.server.Server;
import com.particle.game.world.level.WorldService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.level.Level;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@ParentCommand("dev-switch")
@CommandPermission(CommandPermissionConstant.SWITCH)
@Deprecated
public class SwitchLevelCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(SwitchLevelCommand.class);

    @Inject
    private PlayerService playerService;

    @Inject
    private Server server;

    @Inject
    private WorldService worldService;


    @SubCommand("level")
    public void level(CommandSource source, String playerName, String levelName) {
        if (source == null) {
            logger.error(String.format("为player[%s]执行give指令时，commandSource为空！", playerName));
            return;
        }
        Player player = server.getPlayer(playerName);
        if (player == null) {
            String message = String.format("玩家对象【%s】不存在!", playerName);
            source.sendError(message);
            return;
        }
        Level oldLevel = player.getLevel();
        if (oldLevel == null) {
            String message = String.format("玩家对象【%s】不在任何一个世界中!", playerName);
            source.sendError(message);
            return;
        }
        Level newLevel = this.worldService.getLevel(levelName);
        if (newLevel == null) {
            String message = String.format("指定世界【%s】不存在!", levelName);
            source.sendError(message);
            return;
        }
        this.playerService.switchLevel(player, levelName);
        source.sendError("切换成功");
    }
}
