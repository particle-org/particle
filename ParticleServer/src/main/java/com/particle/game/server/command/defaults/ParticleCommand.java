package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.level.LevelEventType;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.ParticleType;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.GIVE)
@ParentCommand("dev")
@Deprecated
public class ParticleCommand extends BaseCommand {

    private final static Logger logger = LoggerFactory.getLogger(PlaySoundCommand.class);

    @Inject
    private PositionService positionService;

    @Inject
    private ParticleService particleService;

    @SubCommand("particleall")
    public void particleAll(CommandSource commandSource) {
        Player player = commandSource.getPlayer();
        if (player == null) {
            logger.info("playsound拿不到玩家对象");
            return;
        }
        Vector3f pos = positionService.getPosition(player);
        Vector3f position = pos.add(6f, 0, 3f);

        new Thread() {
            @Override
            public void run() {
                LevelEventType[] levelEventTypes = LevelEventType.values();
                int length = levelEventTypes.length;
                int index = 0;
                for (LevelEventType levelEventType : levelEventTypes) {
                    if (levelEventType.getType() >= 2000 && levelEventType.getType() < 3000) {
                        commandSource.sendMessage(String.format("[%s/%s] 收看：%s, 的levelEvent粒子效果", ++index, length,
                                levelEventType));
                        particleService.playParticle(player, levelEventType, position);
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }

                }

                ParticleType[] particleTypes = ParticleType.values();
                length = levelEventTypes.length;
                index = 0;
                for (ParticleType particleType : particleTypes) {
                    commandSource.sendMessage(String.format("[%s/%s] 收看：%s, 的levelEvent声音", ++index, length,
                            particleType));
                    particleService.playParticle(player, particleType, position);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
