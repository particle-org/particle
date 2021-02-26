package com.particle.game.sound;

import com.particle.api.sound.SoundServiceApi;
import com.particle.game.sound.task.SoundTask;
import com.particle.game.ui.task.UITaskHandler;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.entity.EntityType;
import com.particle.model.level.Level;
import com.particle.model.level.LevelEventType;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.*;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundBinder;
import com.particle.model.sound.SoundId;
import com.particle.model.sound.SoundType;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class SoundService implements SoundServiceApi {

    private static final Logger logger = LoggerFactory.getLogger(SoundService.class);

    @Inject
    private NetworkManager networkManager;

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Inject
    private UITaskHandler uiTaskHandler;

    // --------------------- play sound 相关 -------------------------------

    /**
     * 播放声音
     *
     * @param player
     * @param dataPacket
     */
    public void playSound(Player player, DataPacket dataPacket) {
        this.networkManager.sendMessage(player.getClientAddress(), dataPacket);
    }

    /**
     * 播放一序列声音
     *
     * @param player
     * @param soundBinders
     */
    public void playSequenceSound(Player player, List<SoundBinder> soundBinders) {
        SoundTask soundTask = new SoundTask(this, player, soundBinders);
        uiTaskHandler.addTaskQueue(soundTask);
    }

    @Override
    public void playSound(Player player, SoundType soundType, Vector3 position) {
        this.playSound(player, soundType, position, 1.0f, 1.0f);
    }


    @Override
    public void playSound(Player player, SoundType soundType, Vector3 position, float volume) {
        this.playSound(player, soundType, position, volume, 1.0f);
    }

    @Override
    public void playSound(Player player, SoundType soundType, Vector3 position, float volume, float pitch) {
        PlaySoundPacket playSoundPacket = new PlaySoundPacket();
        playSoundPacket.setName(soundType.getName());
        playSoundPacket.setPosition(position);
        playSoundPacket.setVolume(volume);
        playSoundPacket.setPitch(pitch);
        networkManager.sendMessage(player.getClientAddress(), playSoundPacket);
    }

    @Override
    public void broadcastPlaySound(Level level, SoundType soundType, Vector3 position) {
        this.broadcastPlaySound(level, soundType, position, 1.0f, 1.0f);
    }

    @Override
    public void broadcastPlaySound(Level level, SoundType soundType, Vector3 position, float volume) {
        this.broadcastPlaySound(level, soundType, position, volume, 1.0f);
    }

    @Override
    public void broadcastPlaySound(Level level, SoundType soundType, Vector3 position, float volume, float pitch) {
        PlaySoundPacket playSoundPacket = new PlaySoundPacket();
        playSoundPacket.setName(soundType.getName());
        playSoundPacket.setPosition(position);
        playSoundPacket.setVolume(volume);
        playSoundPacket.setPitch(pitch);
        this.broadcastServiceProxy.broadcast(level, position, playSoundPacket, 1);
    }

    @Override
    public void stopSound(Player player, SoundType soundType, boolean stopAllSounds) {
        StopSoundPacket stopSoundPacket = new StopSoundPacket();
        stopSoundPacket.setSoundName(soundType.getName());
        stopSoundPacket.setStopAllSounds(stopAllSounds);
        this.networkManager.sendMessage(player.getClientAddress(), stopSoundPacket);
    }

    @Override
    public void stopSound(Player player, SoundType soundType) {
        this.stopSound(player, soundType, false);
    }

    @Override
    public void broadcastStopSound(Level level, Vector3 position, SoundType soundType, boolean stopAllSounds) {
        StopSoundPacket stopSoundPacket = new StopSoundPacket();
        stopSoundPacket.setSoundName(soundType.getName());
        stopSoundPacket.setStopAllSounds(stopAllSounds);
        this.broadcastServiceProxy.broadcast(level, position, stopSoundPacket, 1);
    }

    @Override
    public void broadcastStopSound(Level level, Vector3 position, SoundType soundType) {
        this.broadcastStopSound(level, position, soundType, false);
    }

    // --------------------- Level Sound Event V2 Packet 相关 -------------------------------

    @Override
    public void levelSound(Player player, Vector3f position, SoundId soundId, int data,
                           EntityType entityType, boolean babyMob, boolean global) {
        LevelSoundEventV2Packet levelSoundEventV2Packet = new LevelSoundEventV2Packet();
        levelSoundEventV2Packet.setEventId((byte) soundId.getEventId());
        levelSoundEventV2Packet.setPosition(position);
        levelSoundEventV2Packet.setData(data);
        levelSoundEventV2Packet.setBabyMob(babyMob);
        levelSoundEventV2Packet.setGlobal(global);
        levelSoundEventV2Packet.setActorIdentifier(entityType.actorType());

        this.networkManager.sendMessage(player.getClientAddress(), levelSoundEventV2Packet);
    }

    @Override
    public void levelSound(Player player, Vector3f position, SoundId soundId, int data, EntityType entityType, boolean babyMob) {
        this.levelSound(player, position, soundId, data, entityType, babyMob, false);
    }

    @Override
    public void levelSound(Player player, Vector3f position, SoundId soundId, int data, EntityType entityType) {
        this.levelSound(player, position, soundId, data, entityType, false, false);
    }

    @Override
    public void levelSound(Player player, Vector3f position, SoundId soundId, int data) {
        this.levelSound(player, position, soundId, data, EntityType.UNDEFINED, false, false);
    }


    @Override
    public void levelSound(Player player, Vector3f position, SoundId soundId) {
        this.levelSound(player, position, soundId, -1, EntityType.UNDEFINED, false, false);
    }

    @Override
    public void broadcastLevelSound(Level level, Vector3f position, SoundId soundId, int data, EntityType entityType, boolean babyMob, boolean global) {
        // 这部分包似乎客户端还在调整，因此在接口内决定具体发的包的类型
        // 上层业务不需要知道发的是什么包，只需传递soundId即可
        switch (soundId) {
            case Place:
                LevelSoundEventPacket levelSoundEventPacket = new LevelSoundEventPacket();
                levelSoundEventPacket.setEventId((byte) soundId.getEventId());
                levelSoundEventPacket.setPosition(position);
                levelSoundEventPacket.setData(data);
                levelSoundEventPacket.setBabyMob(babyMob);
                levelSoundEventPacket.setGlobal(global);
                levelSoundEventPacket.setActorIdentifier(entityType.actorType());
                this.broadcastServiceProxy.broadcast(level, new Vector3(position), levelSoundEventPacket, 1);
                break;
            default:
                LevelSoundEventV2Packet levelSoundEventV2Packet = new LevelSoundEventV2Packet();
                levelSoundEventV2Packet.setEventId((byte) soundId.getEventId());
                levelSoundEventV2Packet.setPosition(position);
                levelSoundEventV2Packet.setData(data);
                levelSoundEventV2Packet.setBabyMob(babyMob);
                levelSoundEventV2Packet.setGlobal(global);
                levelSoundEventV2Packet.setActorIdentifier(entityType.actorType());
                this.broadcastServiceProxy.broadcast(level, new Vector3(position), levelSoundEventV2Packet, 1);
        }
    }

    @Override
    public void broadcastLevelSound(Level level, Vector3f position, SoundId soundId, int data, EntityType entityType, boolean babyMob) {
        this.broadcastLevelSound(level, position, soundId, data, entityType, babyMob, false);
    }

    @Override
    public void broadcastLevelSound(Level level, Vector3f position, SoundId soundId, int data, EntityType entityType) {
        this.broadcastLevelSound(level, position, soundId, data, entityType, false, false);
    }

    @Override
    public void broadcastLevelSound(Level level, Vector3f position, SoundId soundId, int data) {
        this.broadcastLevelSound(level, position, soundId, data, EntityType.UNDEFINED, false, false);
    }


    @Override
    public void broadcastLevelSound(Level level, Vector3f position, SoundId soundId) {
        this.broadcastLevelSound(level, position, soundId, -1, EntityType.UNDEFINED, false, false);
    }

    // --------------------- Level Event Packet 相关 -------------------------------

    @Override
    public void levelSound(Player player, Vector3f position, LevelEventType soundEventType, int data) {
        if (!LevelEventType.isSound(soundEventType)) {
            return;
        }
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.setEventType(soundEventType.getType());
        levelEventPacket.setPosition(position);
        levelEventPacket.setData(data);
        networkManager.sendMessage(player.getClientAddress(), levelEventPacket);
    }

    @Override
    public void levelSound(Player player, Vector3f position, LevelEventType soundEventType) {
        this.levelSound(player, position, soundEventType, 0);
    }

    @Override
    public void broadcastLevelSound(Level level, Vector3f position, LevelEventType soundEventType, int data) {
        if (!LevelEventType.isSound(soundEventType)) {
            return;
        }
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.setEventType(soundEventType.getType());
        levelEventPacket.setPosition(position);
        levelEventPacket.setData(data);
        this.broadcastServiceProxy.broadcast(level, new Vector3(position), levelEventPacket, 1);
    }

    @Override
    public void broadcastLevelSound(Level level, Vector3f position, LevelEventType soundEventType) {
        this.broadcastLevelSound(level, position, soundEventType, 0);
    }
}
