package com.particle.game.server.command.defaults.test;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.ui.PromptService;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.CustomParticleType;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.Future;

@RegisterCommand
@Singleton
@ParentCommand("particle")
@CommandPermission(CommandPermissionConstant.TEST)
public class ParticleTestCommand extends BaseCommand {

    @Inject
    private NetworkManager networkManager;

    @Inject
    private PositionService positionService;

    @Inject
    private ParticleService particleService;

    @Inject
    private PromptService promptService;

    @SubCommand("test")
    public void test(CommandSource source) {
        Player player = source.getPlayer();

        if (player == null) {
            return;
        }

        Vector3f position = this.positionService.getPosition(player);


        for (int i = 0; i < 100; i++) {
            this.particleService.playParticle(player.getLevel(), CustomParticleType.BUBBLE_COLUMN_UP_PARTICLE, position);
        }
    }

    @SubCommand("testAll")
    public void testAll(CommandSource source) {
        Player player = source.getPlayer();

        if (player == null) {
            return;
        }

        Vector3f position = this.positionService.getPosition(player);

        Future particlePlayTask = player.getLevel().getLevelSchedule().scheduleRepeatingTask("ParticleTest", () -> {
            CustomParticleType[] customParticleTypes = CustomParticleType.values();
            for (int x = 0; x < 10; x++) {
                for (int z = 0; z < 20; z++) {
                    int index = x * 20 + z;
                    if (index >= customParticleTypes.length) {
                        return;
                    }

                    this.particleService.playParticle(player.getLevel(), customParticleTypes[index], position.add(x * 5, 0, z * 5));
                }
            }
        }, 3000);

        CustomParticleType[] customParticleTypes = CustomParticleType.values();
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 20; z++) {
                int index = x * 20 + z;
                if (index < customParticleTypes.length) {
                    this.promptService.prompt(player, position.add(x * 5, 0, z * 5), 5 * 60 * 1000, customParticleTypes[index].name());
                }
            }
        }

        player.getLevel().getLevelSchedule().scheduleDelayTask("Remove Test Particle", () -> {
            particlePlayTask.cancel(false);
        }, 5 * 60 * 1000);
    }
}
