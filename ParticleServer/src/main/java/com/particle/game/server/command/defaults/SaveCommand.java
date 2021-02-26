package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.server.Server;
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

@RegisterCommand
@Singleton
@ParentCommand("save")
@CommandPermission(CommandPermissionConstant.TEST)
public class SaveCommand extends BaseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveCommand.class);

    /**
     * 存档同步时间，让1分钟前存档的玩家全部重新存档
     */
    private static final long SAVE_OFFSET = 1000 * 60 * 4;

    @Inject
    private Server server;

    @SubCommand("all")
    public void all(CommandSource source) {
        for (Player player : this.server.getAllPlayers()) {
            player.setLastSaveTime(player.getLastSaveTime() - SAVE_OFFSET);
        }


    }

}
