package com.particle.game.server.command.defaults.test;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.block.types.BlockPrototypeDictionary;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.UpdateBlockPacket;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import com.particle.model.player.PlayerState;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@ParentCommand("testBlockId")
@CommandPermission(CommandPermissionConstant.TEST)
public class BlockTestCommand extends BaseCommand {

    @Inject
    private NetworkManager networkManager;

    @Inject
    private PositionService positionService;

    @Inject
    private LevelService levelService;

    @Inject
    private BlockPrototypeDictionary blockPrototypeDictionary;

    @SubCommand("default")
    public void defaultT(CommandSource source) {
        Player player = source.getPlayer();

        if (player == null) {
            return;
        }

        Vector3f position = this.positionService.getPosition(player);

        int startX = position.getFloorX() - 96 / 2;
        int startY = position.getFloorY();
        int startZ = position.getFloorZ() - 96 / 2;

        for (int x = 0; x < 96; x++) {
            for (int z = 0; z < 96; z++) {
                UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
                updateBlockPacket.setVector3(new Vector3(startX + x, startY, startZ + z));
                updateBlockPacket.setRuntimeId(x * 96 + z);
                updateBlockPacket.setFlag(UpdateBlockPacket.All);
                updateBlockPacket.setLayer(UpdateBlockPacket.DATA_LAYER_NORMAL);

                if (player.getPlayerState() == PlayerState.SPAWNED) {
                    this.networkManager.sendMessage(player.getClientAddress(), updateBlockPacket);
                }
            }
        }
    }

    @SubCommand("Apply")
    public void apply(CommandSource source) {
        Player player = source.getPlayer();

        if (player == null) {
            return;
        }

        Vector3f position = this.positionService.getPosition(player);

        int startX = position.getFloorX() - 96 / 2;
        int startY = position.getFloorY();
        int startZ = position.getFloorZ() - 96 / 2;

        for (int x = 0; x < 96; x++) {
            for (int z = 0; z < 96; z++) {
                UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
                updateBlockPacket.setVector3(new Vector3(startX + x, startY, startZ + z));
                updateBlockPacket.setRuntimeId(x * 96 + z);
                updateBlockPacket.setFlag(UpdateBlockPacket.All);
                updateBlockPacket.setLayer(UpdateBlockPacket.DATA_LAYER_NORMAL);

                if (player.getPlayerState() == PlayerState.SPAWNED) {
                    this.networkManager.sendMessage(player.getClientAddress(), updateBlockPacket);
                }
            }
        }
    }

    @SubCommand("Set")
    public void set(CommandSource source) {
        Player player = source.getPlayer();

        if (player == null) {
            return;
        }

        Vector3f position = this.positionService.getPosition(player);

        levelService.setBlockAt(player.getLevel(), Block.getBlock(BlockPrototype.BEDROCK), new Vector3(position));
    }


}
