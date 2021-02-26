package com.particle.game.ui.task;

import com.particle.model.player.Player;

public interface ITask {

    Player getHolder();

    boolean isValid();

    void onRun();

    void cancel();

    TaskType getTaskType();

}
