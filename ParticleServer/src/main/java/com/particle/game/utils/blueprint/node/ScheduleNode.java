package com.particle.game.utils.blueprint.node;

import com.particle.executor.thread.IScheduleThread;
import com.particle.game.utils.blueprint.context.BackgroundContext;

public abstract class ScheduleNode<T extends BackgroundContext> extends BaseTaskNode<T> {

    public ScheduleNode(String taskName) {
        super(taskName);
    }

    protected abstract IScheduleThread getScheduleThread(T context);

    @Override
    public void run(T context) {
        IScheduleThread scheduleThread = getScheduleThread(context);
        if (Thread.currentThread().getId() == scheduleThread.getId()) {
            this.next(context);
        } else {
            scheduleThread.scheduleSimpleTask(this.getTaskName(), () -> next(context));
        }
    }
}
