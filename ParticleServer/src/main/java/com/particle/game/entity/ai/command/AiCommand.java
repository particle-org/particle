package com.particle.game.entity.ai.command;

import com.particle.api.command.BaseCommand;
import com.particle.game.entity.ai.factory.AiConfigLoader;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
public class AiCommand extends BaseCommand {

    @Inject
    private AiConfigLoader aiConfigLoader;

    @SubCommand("reloadAi")
    public void reload() {
        this.aiConfigLoader.reloadConfig();
    }
}
