package com.particle.api.ui;

import com.particle.model.level.Level;
import com.particle.model.network.packets.data.SetTitlePacket;
import com.particle.model.player.Player;

public interface TitleServiceAPI {

    /**
     * 清理title消息
     *
     * @param player 玩家
     */
    public void clearTitle(Player player);

    /**
     * 清理title消息
     *
     * @param level 世界
     */
    public void clearTitle(Level level);

    /**
     * reset title
     *
     * @param player 玩家
     */
    public void resetTitle(Player player);

    /**
     * @param level 世界
     */
    public void resetTitle(Level level);

    /**
     * @param player 玩家
     * @param title  标题
     */
    public void setTitle(Player player, String title);

    public void setTitle(Player player, String title, int fadeIn, int stay, int fadeOut);

    /**
     * @param level 世界
     * @param title 标题
     */
    public void setTitle(Level level, String title);

    public void setTitle(Level level, String title, int fadeIn, int stay, int fadeOut);

    /**
     * @param player   玩家
     * @param subTitle 子标题
     */
    public void setSubTitle(Player player, String subTitle);

    public void setSubTitle(Player player, String subTitle, int fadeIn, int stay, int fadeOut);

    /**
     * @param level    世界
     * @param subTitle 子标题
     */
    public void setSubTitle(Level level, String subTitle);

    public void setSubTitle(Level level, String subTitle, int fadeIn, int stay, int fadeOut);

    public void setTitleAndSubTitle(Level level, String title, String subTitle, int fadeIn, int stay, int fadeOut);

    public void setTitleAndSubTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut);

    /**
     * @param player 玩家
     * @param title  标题
     */
    public void setActionBar(Player player, String title);

    /**
     * @param player
     * @param actionBar
     * @param keep
     */
    public void setActionBar(Player player, String actionBar, int keep);

    /**
     * @param level 世界
     * @param title 标题
     */
    public void setActionBar(Level level, String title);


    /**
     * @param title
     */
    public SetTitlePacket getActionBarPacket(String title);

    /**
     * @param player   玩家
     * @param fadeIn   进入时间
     * @param stayTime 停留时间
     * @param fadeOut  退出时间
     */
    public void setTimes(Player player, int fadeIn, int stayTime, int fadeOut);


    /**
     * @param level    世界
     * @param fadeIn   进入时间
     * @param stayTime 停留时间
     * @param fadeOut  退出时间
     */
    public void setTimes(Level level, int fadeIn, int stayTime, int fadeOut);
}
