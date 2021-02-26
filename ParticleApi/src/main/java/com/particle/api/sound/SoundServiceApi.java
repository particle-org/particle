package com.particle.api.sound;

import com.particle.model.entity.EntityType;
import com.particle.model.level.Level;
import com.particle.model.level.LevelEventType;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundBinder;
import com.particle.model.sound.SoundId;
import com.particle.model.sound.SoundType;

import java.util.List;

public interface SoundServiceApi {

    /**
     * 播放一序列声音
     *
     * @param player
     * @param soundBinders
     */
    public void playSequenceSound(Player player, List<SoundBinder> soundBinders);

    /**
     * 播放声音
     *
     * @param player
     * @param soundType
     * @param position
     */
    void playSound(Player player, SoundType soundType, Vector3 position);

    /**
     * 播放声音
     *
     * @param player
     * @param soundType
     * @param position
     * @param volume
     */
    void playSound(Player player, SoundType soundType, Vector3 position, float volume);

    /**
     * 播放声音
     *
     * @param player
     * @param soundType
     * @param position
     * @param volume
     * @param pitch
     */
    void playSound(Player player, SoundType soundType, Vector3 position, float volume, float pitch);

    /**
     * 播放声音
     *
     * @param level
     * @param soundType
     * @param position
     */
    void broadcastPlaySound(Level level, SoundType soundType, Vector3 position);

    /**
     * 播放声音
     *
     * @param level
     * @param soundType
     * @param position
     * @param volume
     */
    void broadcastPlaySound(Level level, SoundType soundType, Vector3 position, float volume);

    /**
     * 广播声音给周边半径为1的区块的玩家
     *
     * @param level
     * @param soundType
     * @param position
     * @param volume
     * @param pitch
     */
    void broadcastPlaySound(Level level, SoundType soundType, Vector3 position, float volume, float pitch);

    /**
     * 播放声音
     *
     * @param player
     * @param soundType
     * @param stopAllSounds
     */
    void stopSound(Player player, SoundType soundType, boolean stopAllSounds);

    /**
     * 播放声音
     *
     * @param player
     * @param soundType
     */
    void stopSound(Player player, SoundType soundType);

    /**
     * 播放声音
     *
     * @param level
     * @param position
     * @param soundType
     * @param stopAllSounds
     */
    void broadcastStopSound(Level level, Vector3 position, SoundType soundType, boolean stopAllSounds);

    /**
     * 播放声音
     *
     * @param level
     * @param position
     * @param soundType
     */
    void broadcastStopSound(Level level, Vector3 position, SoundType soundType);

    /**
     * 播放声音
     *
     * @param player
     * @param soundId
     * @param position
     * @param data
     * @param entityType
     * @param babyMob
     * @param global
     */
    void levelSound(Player player, Vector3f position, SoundId soundId, int data,
                    EntityType entityType, boolean babyMob, boolean global);

    /**
     * 播放声音
     *
     * @param player
     * @param soundId
     * @param position
     * @param data
     * @param entityType
     * @param babyMob
     */
    void levelSound(Player player, Vector3f position, SoundId soundId, int data, EntityType entityType, boolean babyMob);

    /**
     * 播放声音
     *
     * @param player
     * @param soundId
     * @param position
     * @param data
     * @param entityType
     */
    void levelSound(Player player, Vector3f position, SoundId soundId, int data, EntityType entityType);

    /**
     * 播放声音
     *
     * @param player
     * @param soundId
     * @param position
     * @param data
     */
    void levelSound(Player player, Vector3f position, SoundId soundId, int data);

    /**
     * 播放声音
     *
     * @param player
     * @param soundId
     * @param position
     */
    void levelSound(Player player, Vector3f position, SoundId soundId);

    /**
     * 播放声音
     *
     * @param player
     * @param position
     * @param soundEventType
     * @param data
     */
    void levelSound(Player player, Vector3f position, LevelEventType soundEventType, int data);

    /**
     * 播放声音
     *
     * @param player
     * @param position
     * @param soundEventType
     */
    void levelSound(Player player, Vector3f position, LevelEventType soundEventType);

    /**
     * 播放声音
     *
     * @param level
     * @param soundId
     * @param position
     * @param data
     * @param entityType
     * @param babyMob
     * @param global
     */
    void broadcastLevelSound(Level level, Vector3f position, SoundId soundId, int data,
                             EntityType entityType, boolean babyMob, boolean global);

    /**
     * 播放声音
     *
     * @param level
     * @param soundId
     * @param position
     * @param data
     * @param entityType
     * @param babyMob
     */
    void broadcastLevelSound(Level level, Vector3f position, SoundId soundId, int data, EntityType entityType, boolean babyMob);

    /**
     * 播放声音
     *
     * @param level
     * @param soundId
     * @param position
     * @param data
     * @param entityType
     */
    void broadcastLevelSound(Level level, Vector3f position, SoundId soundId, int data, EntityType entityType);

    /**
     * 播放声音
     *
     * @param level
     * @param soundId
     * @param position
     * @param data
     */
    void broadcastLevelSound(Level level, Vector3f position, SoundId soundId, int data);

    /**
     * 播放声音
     *
     * @param level
     * @param soundId
     * @param position
     */
    void broadcastLevelSound(Level level, Vector3f position, SoundId soundId);

    /**
     * 播放声音
     *
     * @param level
     * @param position
     * @param soundEventType
     * @param data
     */
    void broadcastLevelSound(Level level, Vector3f position, LevelEventType soundEventType, int data);

    /**
     * 播放声音
     *
     * @param level
     * @param position
     * @param soundEventType
     */
    void broadcastLevelSound(Level level, Vector3f position, LevelEventType soundEventType);
}
