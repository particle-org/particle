package com.particle.game.utils.blueprint.node;

import com.particle.game.utils.blueprint.context.BackgroundContext;
import com.particle.game.utils.blueprint.task.JobTask;

public class JobNode<T extends BackgroundContext> extends BaseTaskNode<T> {

    private JobTask<T> task;

    public JobNode(String taskName, JobTask<T> task) {
        super(taskName);
        this.task = task;
    }

    @Override
    public void run(T context) {
        this.task.job(context);

        this.next(context);
    }
}
