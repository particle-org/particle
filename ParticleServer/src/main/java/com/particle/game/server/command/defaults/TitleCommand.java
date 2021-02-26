package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.server.command.impl.PlayerCommandSource;
import com.particle.game.ui.TitleService;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.player.Player;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@ParentCommand("dev-title")
@Deprecated
public class TitleCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(TitleCommand.class);

    @Inject
    private TitleService titleService;

    @SubCommand("clear")
    public void clear(CommandSource source) {
        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            this.titleService.clearTitle(player);
        }
    }

    @SubCommand("reset")
    public void reset(CommandSource source) {
        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            this.titleService.resetTitle(player);
        }
    }

    @SubCommand("title")
    public void title(CommandSource source, String title) {
        if (StringUtils.isEmpty(title)) {
            source.sendError("文本为空！");
            return;
        }

        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            this.titleService.setTitle(player, title);
        }
    }

    @SubCommand("subtitle")
    public void subTitle(CommandSource source, String subTitle) {
        if (StringUtils.isEmpty(subTitle)) {
            source.sendError("文本为空！");
            return;
        }

        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            this.titleService.setSubTitle(player, subTitle);
        }
    }


    @SubCommand("actionbar ")
    public void actionBar(CommandSource source, String actionbar) {
        if (StringUtils.isEmpty(actionbar)) {
            source.sendError("文本为空！");
            return;
        }

        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            this.titleService.setActionBar(player, actionbar);
        }
    }

    @SubCommand("actionbar")
    public void actionBar(CommandSource source, String actionbar, String keepStr) {
        if (StringUtils.isEmpty(actionbar)) {
            source.sendError("文本为空！");
            return;
        }

        int keep = Integer.parseInt(keepStr);
        if (!source.isConsole()) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            this.titleService.setActionBar(player, actionbar, keep);
        }
    }


    @SubCommand("times")
    public void times(CommandSource source, String fadeIns, String stayTimes, String fadeOuts) {
        int fadeIn = 20;
        int stayTime = 20;
        int fadeOut = 5;
        if (!StringUtils.isEmpty(fadeIns)) {
            try {
                fadeIn = Integer.parseInt(fadeIns);
            } catch (NumberFormatException e) {

            }
        }

        if (!StringUtils.isEmpty(stayTimes)) {
            try {
                stayTime = Integer.parseInt(stayTimes);
            } catch (NumberFormatException e) {

            }
        }

        if (!StringUtils.isEmpty(fadeOuts)) {
            try {
                fadeOut = Integer.parseInt(fadeOuts);
            } catch (NumberFormatException e) {

            }
        }

        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            Player player = playerCommandSource.getPlayer();
            this.titleService.setTimes(player, fadeIn, stayTime, fadeOut);
        }
    }
}
