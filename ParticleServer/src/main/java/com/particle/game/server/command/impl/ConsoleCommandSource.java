package com.particle.game.server.command.impl;

import com.particle.api.command.CommandSource;
import com.particle.game.server.rcon.RconServiceProvider;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class ConsoleCommandSource implements CommandSource {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleCommandSource.class);

    @Inject
    private RconServiceProvider rconServiceProvider;

    @Override
    public long getClientId() {
        return 0;
    }

    @Override
    public void sendMessage(String message) {
        //todo 控制台加颜色
        this.rconServiceProvider.get().sendMessage(message);
    }

    @Override
    public void sendError(String message) {
        //todo 控制台加颜色
        this.rconServiceProvider.get().sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public boolean hasPermission(Set<String> permissions) {
        return true;
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public boolean isConsole() {
        return true;
    }
}
