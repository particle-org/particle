package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.server.command.impl.PlayerCommandSource;
import com.particle.game.ui.ScoreBoardService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.level.Level;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import com.particle.model.ui.score.IdentityDefinitionType;
import com.particle.model.ui.score.ScorePacketInfo;
import com.particle.model.ui.score.ScoreboardPosition;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.SCOREBOARD)
@ParentCommand("dev-scoreboard")
@Deprecated
public class ScoreBoardCommand extends BaseCommand {

    @Inject
    private ScoreBoardService scoreBoardService;

    @SubCommand("objective")
    public void setObjective(CommandSource source, String objectiveName) {
        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            this.scoreBoardService.setDisplayObjective(player.getLevel(), ScoreboardPosition.SIDEBAR, objectiveName, "钟晓锋显示板");
            source.sendMessage("添加计分板成功！");

        }
    }

    @SubCommand("close")
    public void removeObjective(CommandSource source) {
        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            this.scoreBoardService.close(player.getLevel());
            source.sendMessage("关闭计分板成功！");
        }
    }

    @SubCommand("show")
    public void showObjective(CommandSource source) {
        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            this.scoreBoardService.show(player.getLevel());
            source.sendMessage("打开计分板成功！");
        }
    }

    @SubCommand("add-score")
    public void setScore(CommandSource source, String fakeName) {
        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            Level level = player.getLevel();
            List<ScorePacketInfo> infoList = level.getAllScorePacketInfos();
            ScorePacketInfo info = new ScorePacketInfo();
            info.setObjectiveName(level.getScoreObjective().getObjectiveName());
            info.setScoreValue(200);
            info.setIdentityDefinitionType(IdentityDefinitionType.FakePlayer);
            info.setPlayerUniqueId(player.getRuntimeId());
            info.setFakePlayerName(fakeName);
            infoList.add(info);
            this.scoreBoardService.changeScore(level, infoList);
            source.sendMessage("添加计分成功！ID为：" + info.getScoreBoardId());
        }
    }

    @SubCommand("remove-score")
    public void removeScore(CommandSource source, String boardId) {
        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            Level level = player.getLevel();
            List<ScorePacketInfo> infoList = level.getAllScorePacketInfos();
            ScorePacketInfo info = new ScorePacketInfo(Integer.parseInt(boardId));
            info.setObjectiveName(level.getScoreObjective().getObjectiveName());
            info.setScoreValue(200);
            infoList.remove(info);
            this.scoreBoardService.changeScore(level, infoList);
            source.sendMessage("删除计分成功！");
        }
    }
}
