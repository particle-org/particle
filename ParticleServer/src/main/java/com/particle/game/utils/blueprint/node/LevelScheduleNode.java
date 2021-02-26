package com.particle.game.utils.blueprint.node;

import com.particle.executor.thread.IScheduleThread;
import com.particle.game.utils.blueprint.context.BackgroundContext;

public class LevelScheduleNode<T extends BackgroundContext> extends ScheduleNode<T> {

    public LevelScheduleNode(String taskName) {
        super(taskName);
    }

    @Override
    protected IScheduleThread getScheduleThread(T context) {
        return context.getLevelScheduleThread();
    }

}
