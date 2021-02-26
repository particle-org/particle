package com.particle.game.utils.blueprint.node;

import com.particle.executor.thread.IScheduleThread;
import com.particle.game.utils.blueprint.context.BackgroundContext;

public class NodeScheduleNode<T extends BackgroundContext> extends ScheduleNode<T> {

    public NodeScheduleNode(String taskName) {
        super(taskName);
    }

    @Override
    protected IScheduleThread getScheduleThread(T context) {
        return context.getNodeScheduleThread();
    }

}
