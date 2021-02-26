package com.particle.game.ui.task;

import com.particle.model.player.Player;

public abstract class UITask implements ITask {

    private TaskType taskType;
    protected Player player;
    protected int tickLive;
    protected int tick = 0;

    public UITask(Player player, TaskType taskType, int tickLive) {
        this.player = player;
        this.taskType = taskType;
        this.tickLive = tickLive;
    }


    @Override
    public Player getHolder() {
        return player;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public boolean isValid() {
        return this.tick < this.tickLive;
    }

    /**
     * 每隔一秒运行一次
     */
    @Override
    public void onRun() {
        int residue = this.tickLive - this.tick;
        if (this.tick > 0 && residue % 20 == 0) {
            this.onTick();
        }
        this.tick++;
    }

    protected abstract void onTick();

    @Override
    public void cancel() {

    }


}
