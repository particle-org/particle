package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.player.PlayerService;
import com.particle.game.server.Server;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.GAME_MODE)
public class GamemodeCommand extends BaseCommand {

    private String errorMsg = "命令格式：gamemode <1|0> <playerName>";

    @Inject
    private PlayerService playerService;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private Server server;

    @SubCommand("gamemode")
    public void gamemode(CommandSource source, String gamemode) {
        if (StringUtils.isEmpty(gamemode)) {
            source.sendError(errorMsg);
            return;
        }
        GameMode targetMode = null;
        try {
            int gameMode = Integer.parseInt(gamemode);
            targetMode = GameMode.valueOf(gameMode);
        } catch (Exception e) {
            source.sendError(errorMsg);
            return;
        }
        if (targetMode == null) {
            source.sendError(errorMsg);
            return;
        }
        if (!source.isConsole()) {
            Player player = source.getPlayer();
            this.playerService.changePlayerGameMode(player, targetMode, false);

            metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_INVISIBLE, targetMode == GameMode.CREATIVE_VIEWER, true);

            source.sendMessage("设置游戏模式成功");
        }
    }

    @SubCommand("gamemode")
    @CommandPermission(CommandPermissionConstant.CONSOLE)
    public void gamemode(CommandSource source, String gamemode, String playerName) {
        if (StringUtils.isEmpty(gamemode)) {
            source.sendError(errorMsg);
            return;
        }
        GameMode targetMode = null;
        try {
            int gameMode = Integer.parseInt(gamemode);
            targetMode = GameMode.valueOf(gameMode);
        } catch (Exception e) {
            source.sendError(errorMsg);
            return;
        }
        if (targetMode == null) {
            source.sendError(errorMsg);
            return;
        }
        Player player = this.server.getPlayer(playerName);
        if (player == null) {
            source.sendMessage("玩家不存在");
            return;
        }
        this.playerService.changePlayerGameMode(player, targetMode, false);

        metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_INVISIBLE, targetMode == GameMode.CREATIVE_VIEWER, true);

        source.sendMessage("设置游戏模式成功");
    }
}
