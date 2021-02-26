package com.particle.game.utils.blueprint.node;

import com.particle.game.utils.blueprint.context.BackgroundContext;

public abstract class BaseTaskNode<T extends BackgroundContext> {

    private String taskName;

    private BaseTaskNode<T> next;

    public BaseTaskNode(String taskName) {
        this.taskName = taskName;
    }

    public BaseTaskNode<T> getNextNode() {
        return this.next;
    }

    public void setNextNode(BaseTaskNode<T> next) {
        if (this.next == null) {
            this.next = next;
        } else {
            throw new RuntimeException("Next node has been set!");
        }
    }

    public String getTaskName() {
        return taskName;
    }

    public abstract void run(T context);

    public void next(T context) {
        if (this.next != null) {
            this.next.run(context);
        }
    }
}
