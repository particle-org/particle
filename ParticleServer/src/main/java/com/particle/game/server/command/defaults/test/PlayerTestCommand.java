package com.particle.game.server.command.defaults.test;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@ParentCommand("player")
@CommandPermission(CommandPermissionConstant.TEST)
public class PlayerTestCommand extends BaseCommand {

    @Inject
    private MetaDataService metaDataService;

    @SubCommand("air")
    public void test(CommandSource source, String air, String maxAir) {
        Player player = source.getPlayer();

        if (player == null) {
            return;
        }

        metaDataService.setShortData(player, EntityMetadataType.MAX_AIR, Short.parseShort(maxAir), false);
        metaDataService.setShortData(player, EntityMetadataType.AIR, Short.parseShort(air), false);
        metaDataService.refreshMetadata(player);
    }
}
