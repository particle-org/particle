package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.server.command.impl.PlayerCommandSource;
import com.particle.game.ui.TextService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import com.particle.util.font.TextFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.SAY)
@ParentCommand("dev")
@Deprecated
public class SayCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(SayCommand.class);

    @Inject
    private TextService textService;

    @SubCommand("say")
    public void say(CommandSource source, String args) {
        if (source == null) {
            logger.error(String.format("执行say指令时，commandSource为空！"));
            return;
        }
        if (StringUtils.isEmpty(args)) {
            logger.error(String.format("执行say指令时，说话内容！"));
            return;
        }
        String[] param = new String[0];
        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            this.textService.broadcastRawMessage(TextFormat.RED + "raw message");
            this.textService.broadcastChatMessage("chat message");
            this.textService.broadcastTranslateMessage("translate message [zxf] lifang");
            this.textService.broadcastPopupMessage("popup message [zxf, lifang]");
            this.textService.broadcastJukePopupMessage("jukebox popup message [zxf, xlf]");
            this.textService.broadcastTipMessage("tip message");
            this.textService.broadcastSystemMessage("system message");
            this.textService.broadcastWhisperMessage("whisper message");
            this.textService.broadcastAnnouncement("announcement message");
        }
    }
}
