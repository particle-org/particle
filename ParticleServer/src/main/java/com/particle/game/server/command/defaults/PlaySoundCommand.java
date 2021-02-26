package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.sound.SoundService;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.level.LevelEventType;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundId;
import com.particle.model.sound.SoundType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.GIVE)
@ParentCommand("dev")
@Deprecated
public class PlaySoundCommand extends BaseCommand {

    private final static Logger logger = LoggerFactory.getLogger(PlaySoundCommand.class);

    @Inject
    private PositionService positionService;

    @Inject
    private SoundService soundService;


    @SubCommand("leveleventsound")
    public void leveleventsound(CommandSource commandSource) {
        Player player = commandSource.getPlayer();
        if (player == null) {
            logger.info("playsound拿不到玩家对象");
            return;
        }
        Vector3f position = positionService.getPosition(player);

        new Thread() {
            @Override
            public void run() {
                LevelEventType[] levelEventTypes = LevelEventType.values();
                int length = levelEventTypes.length;
                int index = 0;
                for (LevelEventType levelEventType : levelEventTypes) {
                    if (levelEventType.getType() > 1063) {
                        continue;
                    }
                    commandSource.sendMessage(String.format("[%s/%s] 收听：%s, 的声音", ++index, length,
                            levelEventType));
                    soundService.levelSound(player, position, levelEventType);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @SubCommand("levelallsound")
    public void levelAllsound(CommandSource commandSource) {
        Player player = commandSource.getPlayer();
        if (player == null) {
            logger.info("playsound拿不到玩家对象");
            return;
        }
        Vector3f position = positionService.getPosition(player);

        new Thread() {
            @Override
            public void run() {
                SoundId[] soundIds = SoundId.values();
                int length = soundIds.length;
                int index = 0;
                for (SoundId soundId : soundIds) {
                    commandSource.sendMessage(String.format("[%s/%s] 收听：%s, 的声音", ++index, length,
                            soundId));
                    soundService.levelSound(player, position, soundId);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @SubCommand("playallsound")
    public void playAllSound(CommandSource commandSource) {
        Player player = commandSource.getPlayer();
        if (player == null) {
            logger.info("playsound拿不到玩家对象");
            return;
        }
        Vector3 position = positionService.getFloorPosition(player);
        new Thread() {
            @Override
            public void run() {
                SoundType[] soundTypes = SoundType.values();
                int length = soundTypes.length;
                int index = 0;
                for (SoundType soundType : soundTypes) {
                    commandSource.sendMessage(String.format("[%s/%s] 收听：%s, 类别：%s 的声音", ++index, length,
                            soundType.getName(), soundType.getCategory()));
                    soundService.playSound(player, soundType, position);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                }
            }
        }.start();
    }

    @SubCommand("playsound")
    public void playSound(CommandSource commandSource, String name, int x, int y, int z, float volume, float pitch) {
        Player player = commandSource.getPlayer();
        if (player == null) {
            logger.info("playsound拿不到玩家对象");
            return;
        }
        SoundType soundType = SoundType.fromValue(name);
        if (soundType == null) {
            commandSource.sendError("不存在此声音");
            return;
        }
        Vector3 position = new Vector3(x, y, z);
        this.soundService.playSound(player, soundType, position, 1.0f, 1.0f);
    }

    @SubCommand("playsound")
    public void playSound(CommandSource commandSource, String name, int x, int y, int z, float volume) {
        this.playSound(commandSource, name, x, y, z, volume, 1.0f);
    }

    @SubCommand("playsound")
    public void playSound(CommandSource commandSource, String name, int x, int y, int z) {
        this.playSound(commandSource, name, x, y, z, 1.0f, 1.0f);
    }

    @SubCommand("playsound")
    public void playSound(CommandSource commandSource, String name) {
        Player player = commandSource.getPlayer();
        if (player == null) {
            logger.info("playsound拿不到玩家对象");
            return;
        }
        Vector3 position = positionService.getFloorPosition(player);
        this.playSound(commandSource, name, position.getX(), position.getY(), position.getZ(), 1.0f, 1.0f);
    }
}
