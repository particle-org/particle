package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.world.level.TimeService;
import com.particle.game.world.level.WorldService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.level.Level;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@ParentCommand("time")
@CommandPermission(CommandPermissionConstant.TIME)
public class TimeCommand extends BaseCommand {

    private String errorAddMsg = "time <add> <时间值>";

    private String errorQueryMsg = "time <query> <daytime|gametime|day>";

    private String errorSetMsg = "time <set> <时间值|day|noon|night|midnight>";

    @Inject
    private TimeService timeService;

    @Inject
    private WorldService worldService;

    @SubCommand("add")
    public void add(CommandSource source, String value) {
        if (StringUtils.isEmpty(value)) {
            source.sendError(errorAddMsg);
            return;
        }
        if (!source.isConsole()) {
            Player player = source.getPlayer();

            int addV;
            try {
                addV = Math.max(0, Integer.parseInt(value));
            } catch (Exception e) {
                source.sendError(errorAddMsg);
                return;
            }
            player.getLevel().getLevelSchedule().scheduleSimpleTask("SetTime", () -> {
                this.timeService.setTime(player.getLevel(), player.getLevel().getTime() + addV, true);

                source.sendMessage("时间设置成功");
            });
        }
    }

    @SubCommand("add")
    @CommandPermission(CommandPermissionConstant.CONSOLE)
    public void add(CommandSource source, String levelName, String value) {
        Level level = this.worldService.getLevel(levelName);
        if (level == null) {
            source.sendError("该世界不存在");
            return;
        }
        int addV;
        try {
            addV = Math.max(0, Integer.parseInt(value));
        } catch (Exception e) {
            source.sendError(errorAddMsg);
            return;
        }
        level.getLevelSchedule().scheduleSimpleTask("SetTime", () -> {
            this.timeService.setTime(level, level.getTime() + addV, true);
            source.sendMessage("时间设置成功");
        });
    }

    @SubCommand("query")
    public void query(CommandSource source, String value) {
        if (StringUtils.isEmpty(value)) {
            source.sendError(errorQueryMsg);
            return;
        }
        if (!source.isConsole()) {
            Player player = source.getPlayer();
            this.query(source, player.getLevel(), value);
        }
    }

    @SubCommand("query")
    @CommandPermission(CommandPermissionConstant.CONSOLE)
    public void query(CommandSource source, String levelName, String value) {
        if (StringUtils.isEmpty(value)) {
            source.sendError(errorQueryMsg);
            return;
        }
        Level level = this.worldService.getLevel(levelName);
        if (level == null) {
            source.sendError("该世界不存在");
            return;
        }
        this.query(source, level, value);
    }

    private void query(CommandSource source, Level level, String value) {
        int returnResult;
        if (value.equalsIgnoreCase("daytime")) {
            returnResult = (int) (level.getTime() % 24000);
            source.sendMessage(String.format("当天已经过[%s]个tick!", returnResult));
            return;
        } else if (value.equalsIgnoreCase("gametime")) {
            returnResult = (int) level.getTime();
            source.sendMessage(String.format("该世界已经历[%s]个tick!", returnResult));
            return;
        } else if (value.equalsIgnoreCase("day")) {
            returnResult = (int) (level.getTime() / 24000) + 1;
            source.sendMessage(String.format("该世界已经历[%s]天!", returnResult));
            return;
        } else {
            source.sendError(errorQueryMsg);
            return;
        }

    }

    @SubCommand("set")
    public void set(CommandSource source, String value) {
        if (StringUtils.isEmpty(value)) {
            source.sendError(errorSetMsg);
            return;
        }
        if (!source.isConsole()) {
            Player player = source.getPlayer();
            this.set(source, player.getLevel(), value);
        }
    }

    @SubCommand("set")
    @CommandPermission(CommandPermissionConstant.CONSOLE)
    public void set(CommandSource source, String levelName, String value) {
        if (StringUtils.isEmpty(value)) {
            source.sendError(errorSetMsg);
            return;
        }

        Level level = this.worldService.getLevel(levelName);
        if (level == null) {
            source.sendError("该世界不存在");
            return;
        }
        this.set(source, level, value);
    }

    private void set(CommandSource source, Level level, String value) {
        int addV;
        if (value.equalsIgnoreCase("day")) {
            addV = 1000;
        } else if (value.equalsIgnoreCase("noon")) {
            addV = 6000;
        } else if (value.equalsIgnoreCase("night")) {
            addV = 13000;
        } else if (value.equalsIgnoreCase("midnight")) {
            addV = 18000;
        } else {
            try {
                addV = Math.max(0, Integer.parseInt(value));
            } catch (Exception e) {
                source.sendError(errorSetMsg);
                return;
            }
        }
        level.getLevelSchedule().scheduleSimpleTask("SetTime", () -> {
            this.timeService.setTime(level, addV, true);

            source.sendMessage("时间设置成功");
        });
    }
}
