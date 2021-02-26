package com.particle.game.world.aoi.components;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.entity.Entity;
import com.particle.model.level.Chunk;
import com.particle.model.player.Player;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BroadcastModule extends BehaviorModule {

    /**
     * 该生物spawn的区块
     */
    private Chunk registerChunk;

    /**
     * 该生物是否可以被其它生物接触
     */
    private boolean untouchable = false;

    /**
     * 该生物可见的玩家
     */
    private Set<Player> subscripterList = new HashSet<>();

    /**
     * 该生物可见玩家的白名单
     */
    private Set<Long> subscripterWhiteList = new HashSet<>();
    private Set<Long> subscripterWhiteListView = Collections.unmodifiableSet(subscripterWhiteList);
    private boolean enableWhiteList = false;

    /**
     * 最近一次更新时间戳
     */
    private long lastUpdateTimestamp;

    /**
     * 更新生物spawn的区块
     *
     * @param chunk
     * @return
     */
    public Chunk spawnAt(Chunk chunk) {
        Chunk spawnedChunk = this.registerChunk;

        this.registerChunk = chunk;

        return spawnedChunk;
    }

    /**
     * 查询生物spawn的区块
     *
     * @return
     */
    public Chunk getRegisterChunk() {
        return registerChunk;
    }

    /**
     * 重置生物spawn的区块
     *
     * @return
     */
    public Chunk resetSpawnedChunk() {
        Chunk spawnedChunk = this.registerChunk;

        this.registerChunk = null;

        return spawnedChunk;
    }

    /**
     * 检查该生物是否可见玩家
     *
     * @param player
     * @return
     */
    public boolean canSee(Player player) {
        return this.subscripterList.contains(player);
    }

    /**
     * 增加该生物可见的玩家
     * 该数据随区块订阅过程更新
     *
     * @param player
     */
    public void addDisplayedPlayer(Player player) {
        this.subscripterList.add(player);
    }

    /**
     * 移除该生物可见的玩家
     * 该数据随区块订阅过程更新
     *
     * @param player
     */
    public void removeDisplayedPlayer(Player player) {
        this.subscripterList.remove(player);
    }

    /**
     * 增加该生物可见的玩家
     * 该数据随区块订阅过程更新
     *
     * @param players
     */
    public void addDisplayedPlayers(Set<Player> players) {
        this.subscripterList.addAll(players);
    }

    /**
     * 移除该生物可见的玩家
     * 该数据随区块订阅过程更新
     *
     * @param players
     */
    public void removeDisplayedPlayers(Set<Player> players) {
        for (Player player : players) {
            this.subscripterList.remove(player);
        }
    }

    /**
     * 迭代所有可见玩家
     *
     * @param forEachInterface
     */
    public void forEachDisplayedPlayers(DisplayedPlayersForEachInterface forEachInterface) {
        this.subscripterList.forEach(player -> forEachInterface.next(player.getClientAddress(), player));
    }

    /**
     * 迭代所有观察者的迭代器
     */
    public interface DisplayedPlayersForEachInterface {
        void next(InetSocketAddress address, Entity entity);
    }

    /**
     * 清空所有的Spawn记录
     */
    public void clearDisplayRecord() {
        this.subscripterList.clear();
    }

    /**
     * 获取白名单
     *
     * @return
     */
    public Set<Long> getSubscripterWhiteList() {
        return subscripterWhiteList;
    }

    public Set<Long> getSubscripterWhiteListView() {
        return subscripterWhiteListView;
    }

    /**
     * 判断白名单是否开启
     *
     * @return
     */
    public boolean isEnableWhiteList() {
        return enableWhiteList;
    }

    /**
     * 设置白名单是否开启
     *
     * @param enableWhiteList
     */
    public void setEnableWhiteList(boolean enableWhiteList) {
        this.enableWhiteList = enableWhiteList;
    }

    /**
     * 判断是否可以接触
     *
     * @return
     */
    public boolean isUntouchable() {
        return untouchable;
    }

    /**
     * 设置是否可以接触
     *
     * @param untouchable
     */
    public void setUntouchable(boolean untouchable) {
        this.untouchable = untouchable;
    }

    public Set<Player> getSubscriptList() {
        return this.subscripterList;
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }
}
