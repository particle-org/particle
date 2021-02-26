package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.server.command.CommandManager;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.HelpCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.permission.CommandPermissionConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.CONSOLE)
public class RootHelpCommand extends BaseCommand {

    private Logger logger = LoggerFactory.getLogger(RootHelpCommand.class);

    @Inject
    private CommandManager commandManager;

    @HelpCommand
    public void help(CommandSource sender) {
        StringBuilder sb = new StringBuilder();
        sb.append("please input command below:\r\n");
        Set<String> cmdName = commandManager.getAllCmdNames();
        for (String cmd : cmdName) {
            sb.append(cmd).append("\r\n");
        }
        sender.sendMessage(sb.toString());
    }

}
