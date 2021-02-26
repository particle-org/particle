package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.server.Server;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.CONSOLE)
public class PlayerAmountUpdateCommand extends BaseCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerAmountUpdateCommand.class);

    @Inject
    private Server server;

    @SubCommand("playerAmount")
    public void createGroup(CommandSource source, String amount) {
        this.server.setMaxPlayerAmount(Integer.parseInt(amount));
    }

}
