package com.particle.game.server.command.defaults.test;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.world.level.ChunkService;
import com.particle.game.world.level.WorldService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.TEST)
public class ChunkTestCommand extends BaseCommand {

    @Inject
    private ChunkService chunkService;

    @Inject
    private WorldService worldService;

    @SubCommand("chunkTest")
    public void test(CommandSource source, String strX, String strZ) {
        int chunkX = Integer.parseInt(strX);
        int chunkZ = Integer.parseInt(strZ);

        this.chunkService.reloadChunk(this.worldService.getDefaultLevel(), chunkX, chunkZ);
    }

}
