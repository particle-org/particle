package com.particle.game.utils.blueprint.context;

import com.particle.executor.thread.IScheduleThread;
import com.particle.model.player.Player;

public class BackgroundContext {
    private Player player;
    private IScheduleThread nodeScheduleThread;
    private IScheduleThread levelScheduleThread;

    public BackgroundContext(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public IScheduleThread getNodeScheduleThread() {
        return nodeScheduleThread;
    }

    public void setNodeScheduleThread(IScheduleThread nodeScheduleThread) {
        this.nodeScheduleThread = nodeScheduleThread;
    }

    public IScheduleThread getLevelScheduleThread() {
        return levelScheduleThread;
    }

    public void setLevelScheduleThread(IScheduleThread levelScheduleThread) {
        this.levelScheduleThread = levelScheduleThread;
    }
}
