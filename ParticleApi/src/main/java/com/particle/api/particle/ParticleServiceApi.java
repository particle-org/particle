package com.particle.api.particle;

import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.level.LevelEventType;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.CustomParticleType;
import com.particle.model.particle.ParticleType;
import com.particle.model.player.Player;

public interface ParticleServiceApi {
    /**
     * 播放粒子效果
     *
     * @param player         玩家
     * @param levelEventType 类型
     * @param position       位置
     * @param data           数据
     */
    void playParticle(Player player, LevelEventType levelEventType, Vector3f position, int data);

    /**
     * 播放粒子效果
     *
     * @param player         玩家
     * @param levelEventType 类型
     * @param position       位置
     */
    void playParticle(Player player, LevelEventType levelEventType, Vector3f position);

    /**
     * 播放粒子效果
     *
     * @param level          世界
     * @param levelEventType 类型
     * @param position       位置
     * @param data           数据
     */
    void playParticle(Level level, LevelEventType levelEventType, Block block, Vector3f position, int data);

    /**
     * @param level          世界
     * @param levelEventType 类型
     * @param position       位置
     */
    void playParticle(Level level, LevelEventType levelEventType, Vector3f position);

    /**
     * 播放粒子效果
     *
     * @param player       玩家
     * @param particleType 类型
     * @param position     位置
     * @param data         数据
     */
    void playParticle(Player player, ParticleType particleType, Vector3f position, int data);

    /**
     * 播放粒子效果
     *
     * @param player       玩家
     * @param particleType 类型
     * @param position     位置
     */
    void playParticle(Player player, ParticleType particleType, Vector3f position);

    /**
     * 播放粒子效果
     *
     * @param level        世界
     * @param particleType 类型
     * @param position     位置
     * @param data         数据
     */
    void playParticle(Level level, ParticleType particleType, Vector3f position, int data);

    /**
     * 播放粒子效果
     *
     * @param level        世界
     * @param particleType 类型
     * @param position     位置
     */
    void playParticle(Level level, ParticleType particleType, Vector3f position);

    /**
     * 播放新粒子效果
     *
     * @param level
     * @param customParticleType
     * @param position
     */
    void playParticle(Level level, CustomParticleType customParticleType, Vector3f position);

    /**
     * 播放新粒子效果
     *
     * @param level
     * @param customParticleType
     * @param fromPlayer
     * @param position
     */
    void playParticle(Level level, CustomParticleType customParticleType, Player fromPlayer, Vector3f position);
}
