package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.entity.attribute.explevel.ExperienceService;
import com.particle.game.server.Server;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.XP)
public class XpCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(XpCommand.class);

    @Inject
    private Server server;

    @Inject
    private ExperienceService experienceService;

    @SubCommand("xp")
    public void xp(CommandSource source, String value, String playerName) {
        if (source == null) {
            logger.error(String.format("为player[%s]执行give指令时，commandSource为空！", playerName));
            return;
        }
        Player player;
        String errorMessage = "请输入正确的命令格式：xp <mounts>[L] [playerName]";
        player = server.getPlayer(playerName);

        if (player == null) {
            String message = String.format("玩家对象【%s】不存在!", playerName);
            source.sendError(message);
            return;
        }

        boolean isLevel = false;
        if (StringUtils.isEmpty(value)) {
            source.sendError(errorMessage);
            return;
        }

        if (value.endsWith("L") || value.endsWith("l")) {
            value = value.substring(0, value.length() - 1);
            isLevel = true;
        }

        int expValue = 0;
        if (!StringUtils.isEmpty(value)) {
            try {
                expValue = Integer.parseInt(value);
            } catch (NumberFormatException nf) {
                source.sendError(errorMessage);
                return;
            }
        } else {
            source.sendError(errorMessage);
            return;
        }

        if (this.experienceService.addExperience(player, expValue, isLevel)) {
            source.sendMessage(String.format("玩家[%s]获取添加经验成功", playerName));
        } else {
            source.sendError(String.format("玩家[%s]获取添加经验失败", playerName));
        }
    }
}
