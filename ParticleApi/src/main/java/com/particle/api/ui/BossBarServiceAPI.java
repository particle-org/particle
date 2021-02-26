package com.particle.api.ui;

import com.particle.model.player.Player;

import java.awt.*;

public interface BossBarServiceAPI {

    /**
     * 刷新BossBar的文字
     *
     * @param player   玩家
     * @param template 文字
     */
    void updateTemplate(Player player, String template);


    /**
     * 渲染模板中的文字
     *
     * @param player 玩家
     */
    void render(Player player);


    /**
     * 刷新Boss的血量
     *
     * @param player 玩家
     * @param health 血量
     */
    void updateHealth(Player player, float health);


    /**
     * 更新纹理
     *
     * @param player 玩家
     * @param color  颜色
     */
    void updateTexture(Player player, Color color);


    /**
     * 创建BossBar
     *
     * @param player 玩家
     */
    void addBossBar(Player player);


    /**
     * 创建BossBar
     *
     * @param player   玩家
     * @param template 文字
     */
    void addBossBar(Player player, String template);

    /**
     * 去除bossBar
     *
     * @param player 玩家
     */
    void removeBossBar(Player player);
}
