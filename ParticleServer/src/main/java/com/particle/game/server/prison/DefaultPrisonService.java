package com.particle.game.server.prison;

import com.particle.api.server.PrisonServiceApi;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class DefaultPrisonService implements PrisonServiceApi {
    /**
     * 添加监狱数据
     *
     * @param player
     */
    public void imprison(Player player) {
    }

    /**
     * 判断玩家是否在监狱中
     *
     * @param player
     * @return
     */
    public boolean isInJail(Player player) {
        return false;
    }
}
