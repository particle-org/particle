package com.particle.api.ui;

import com.particle.model.level.Level;
import com.particle.model.player.Player;
import com.particle.model.ui.score.ScorePacketInfo;
import com.particle.model.ui.score.ScoreboardPosition;

import java.util.List;

public interface ScoreBoardServiceAPI {

    /**
     * 单个玩家显示他所在世界的计分板
     * 显示计分板
     *
     * @param player 玩家
     */
    public void show(Player player);


    /**
     * 该世界中的所有玩家
     * 显示计分板
     *
     * @param level 世界
     */
    public void show(Level level);

    /**
     * 关闭单个玩家的计分板
     *
     * @param player 玩家
     */
    public void close(Player player);

    /**
     * 关闭世界的计分板
     *
     * @param level 世界
     */
    public void close(Level level);

    /**
     * 外部接口
     * 用来设置世界的计分板
     *
     * @param level            世界
     * @param displaySlot      展示的位置
     * @param objective        唯一性的名称
     * @param objectiveDisplay 显示名称
     */
    public void setDisplayObjective(Level level, ScoreboardPosition displaySlot, String objective, String objectiveDisplay);

    /**
     * 更改数据
     *
     * @param level 世界
     * @param infos 详情
     */
    public void changeScore(Level level, List<ScorePacketInfo> infos);
}
