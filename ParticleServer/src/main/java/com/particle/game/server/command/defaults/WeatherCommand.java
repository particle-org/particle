package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.world.level.WeatherService;
import com.particle.game.world.level.WorldService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.level.Level;
import com.particle.model.level.Weather;
import com.particle.model.math.Vector3f;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@ParentCommand("weather")
@CommandPermission(CommandPermissionConstant.TIME)
public class WeatherCommand extends BaseCommand {

    private String errorMsg = "weather <天气 | rain [rainfall] | thunder | clear> ";

    @Inject
    private WeatherService weatherService;

    @Inject
    private WorldService worldService;

    @SubCommand("rain")
    public void rain(CommandSource source) {
        if (!source.isConsole()) {
            Player player = source.getPlayer();
            // 只是要下雨而已，雨量任意
            player.getLevel().getLevelSchedule().scheduleSimpleTask("WeatherChange", () -> {
                this.weatherService.setWeather(player.getLevel(), Weather.RAIN, 100000, new Vector3f(0, 0, 0), true);
                source.sendMessage("天气设置成功");
            });
        }
    }

    @SubCommand("rain")
    @CommandPermission(CommandPermissionConstant.CONSOLE)
    public void rain(CommandSource source, String levelName, String value) {
        Level level = this.worldService.getLevel(levelName);
        if (level == null) {
            source.sendError("该世界不存在");
            return;
        }

        try {
            int rainfall = Integer.parseInt(value);
            level.getLevelSchedule().scheduleSimpleTask("WeatherChange", () -> {
                this.weatherService.setWeather(level, Weather.RAIN, rainfall, new Vector3f(0, 0, 0), true);
                source.sendMessage("天气设置成功");
            });
        } catch (NumberFormatException e) {
            source.sendError(errorMsg);
            return;
        }
    }

    // 指定雨量
    @SubCommand("rain")
    public void rain(CommandSource source, String value) {
        if (StringUtils.isEmpty(value)) {
            source.sendError(errorMsg);
            return;
        }

        if (!source.isConsole()) {
            Player player = source.getPlayer();
            try {
                int rainfall = Integer.parseInt(value);

                player.getLevel().getLevelSchedule().scheduleSimpleTask("WeatherChange", () -> {
                    this.weatherService.setWeather(player.getLevel(), Weather.RAIN, rainfall, new Vector3f(0, 0, 0), true);

                    source.sendMessage("天气设置成功");
                });
            } catch (NumberFormatException e) {
                source.sendError(errorMsg);
                return;
            }
        }
    }

    @SubCommand("clear")
    public void clear(CommandSource source) {
        if (!source.isConsole()) {
            Player player = source.getPlayer();

            player.getLevel().getLevelSchedule().scheduleSimpleTask("WeatherChange", () -> {
                this.weatherService.setWeather(player.getLevel(), Weather.SUNNY, 0, new Vector3f(0, 0, 0), true);

                source.sendMessage("天气设置成功");
            });
        }
    }

    @SubCommand("clear")
    @CommandPermission(CommandPermissionConstant.CONSOLE)
    public void clear(CommandSource source, String levelName) {
        Level level = this.worldService.getLevel(levelName);
        if (level == null) {
            source.sendError("该世界不存在");
            return;
        }

        level.getLevelSchedule().scheduleSimpleTask("WeatherChange", () -> {
            this.weatherService.setWeather(level, Weather.SUNNY, 0, new Vector3f(0, 0, 0), true);

            source.sendMessage("天气设置成功");
        });
    }
}
