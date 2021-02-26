package com.particle.api;

import com.particle.model.player.Player;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.UUID;

public interface ServerApi {
    /**
     * 主动关闭与玩家的连接
     *
     * @param player the entity
     * @param reason the reason
     */
    void close(Player player, String reason);

    /**
     * 获取玩家
     *
     * @param entityId
     * @return
     */
    Player getPlayer(long entityId);

    /**
     * 获取玩家
     *
     * @param playerName
     * @return
     */
    Player getPlayer(String playerName);

    /**
     * 获取玩家
     *
     * @param uuid
     * @return
     */
    Player getPlayer(UUID uuid);

    /**
     * 获取玩家
     *
     * @param uuid
     * @return
     */
    Player searchOnlinePlayerByUuid(UUID uuid);

    /**
     * 查询已经连接的玩家
     */
    Player getOnlinePlayer(InetSocketAddress address);

    /**
     * 获取所有在线玩家
     *
     * @return
     */
    Collection<Player> getAllPlayers();

    /**
     * 获取玩家数量
     *
     * @return
     */
    int getPlayerAmount();

    /**
     * 获取合适玩家数量
     *
     * @return
     */
    int getSuitablePlayerAmount();

    /**
     * 获取最大玩家数量
     *
     * @return
     */
    int getMaxPlayerAmount();

    /**
     * 设置最大玩家数量
     *
     * @param amount
     */
    void setMaxPlayerAmount(int amount);

    /**
     * 关闭
     *
     * @return
     */
    boolean shutdown();

    /**
     * 暂停
     *
     * @return
     */
    boolean pause();

    /**
     * 重启
     *
     * @return
     */
    boolean restart();

    /**
     * 获取状态
     *
     * @return
     */
    ServerStatus getStatus();

    /**
     * 玩家是否在线
     *
     * @param player
     * @return
     */
    boolean isOnline(Player player);

    /**
     * 状态
     */
    public enum ServerStatus {
        INIT, RUNNING, PAUSED, STOP;
    }
}

