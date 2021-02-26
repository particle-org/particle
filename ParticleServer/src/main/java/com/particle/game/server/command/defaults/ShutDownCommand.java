package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.executor.service.AsyncScheduleService;
import com.particle.game.player.PlayerService;
import com.particle.game.server.Server;
import com.particle.game.server.rcon.PowerService;
import com.particle.game.ui.TextService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.Future;

@RegisterCommand
@Singleton
@ParentCommand("shutdown")
@CommandPermission(CommandPermissionConstant.TEST)
public class ShutDownCommand extends BaseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutDownCommand.class);

    @Inject
    private PowerService powerService;

    @Inject
    private PlayerService playerService;

    @Inject
    private Server server;

    private Future shutdownTask;

    @Inject
    private TextService textService;


    @SubCommand("soft")
    public void soft(CommandSource source) {
        this.powerService.softShutdown();
    }

    @SubCommand("force")
    public void force(CommandSource source) {
        this.powerService.shutdown();
    }

    /**
     * @param source
     * @param limit
     * @param addr
     * @param sport
     */
    @SubCommand("scheduled")
    public void scheduled(CommandSource source, String limit, String addr, String sport) {
        source.sendMessage("已收到shutdown指令");

        // 取消旧指令
        if (this.shutdownTask != null) {
            source.sendMessage("已有Schedule关闭");
            // 停止任务
            this.shutdownTask.cancel(false);
            this.shutdownTask = null;
        }

        // 玩家数量转换
        int playerLimit = Integer.parseInt(limit);

        // 启动定时任务检查
        this.shutdownTask = AsyncScheduleService.getInstance().getThread().scheduleRepeatingTask("ShutdownSchedule", () -> {
            int playerAmount = this.server.getPlayerAmount();
            if (playerAmount <= playerLimit) {
                // 停止任务
                this.shutdownTask.cancel(false);
                this.shutdownTask = null;

                // 重定向玩家
                short port = Short.parseShort(sport);
                for (Player player : this.server.getAllPlayers()) {
                    this.playerService.transfer(player, addr, port);
                }

                // 启动关服任务
                AsyncScheduleService.getInstance().getThread().scheduleDelayTask("Shutdown", () -> {
                    this.powerService.softShutdown();
                }, 5000);
            }
        }, 1000);
    }

    @SubCommand("scheduled_delay")
    public void scheduledDelay(CommandSource source, String limit, String addr, String sport) {
        source.sendMessage("已收到shutdown指令");
        long shutdownTime = System.currentTimeMillis();

        // 取消旧指令
        if (this.shutdownTask != null) {
            source.sendMessage("已有Schedule关闭");
            // 停止任务
            this.shutdownTask.cancel(false);
            this.shutdownTask = null;
        }

        // 玩家数量转换
        int playerLimit = Integer.parseInt(limit);

        // 启动定时任务检查
        this.shutdownTask = AsyncScheduleService.getInstance().getThread().scheduleRepeatingTask("ShutdownSchedule", () -> {
            int playerAmount = this.server.getPlayerAmount();
            long remainTime = System.currentTimeMillis() - shutdownTime;
            if (remainTime / 1000 == 0 || (remainTime / 1000 >= 20 && remainTime / 1000 <= 30)) {
                for (Player player : server.getAllPlayers()) {
                    textService.sendChatMessage(player, String.format("§c本服将在%s秒内重启，您将被传往其他服务器", 30 - remainTime / 1000));
                }
            }

            if (playerAmount <= playerLimit && remainTime >= 30 * 1000) {
                // 停止任务
                this.shutdownTask.cancel(false);
                this.shutdownTask = null;

                // 重定向玩家
                short port = Short.parseShort(sport);
                for (Player player : this.server.getAllPlayers()) {
                    this.playerService.transfer(player, addr, port);
                }

                // 启动关服任务
                AsyncScheduleService.getInstance().getThread().scheduleDelayTask("Shutdown", () -> {
                    this.powerService.softShutdown();
                }, 5000);
            }
        }, 1000);
    }

    @SubCommand("cancelScheduled")
    public void cancelScheduled(CommandSource source) {
        // 停止任务
        this.shutdownTask.cancel(false);
        this.shutdownTask = null;
    }
}
