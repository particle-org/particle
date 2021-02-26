package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;

import javax.inject.Singleton;

@RegisterCommand
@Singleton
public class ListCommand extends BaseCommand {

    @SubCommand("list")
    public void list() {
    }
}
