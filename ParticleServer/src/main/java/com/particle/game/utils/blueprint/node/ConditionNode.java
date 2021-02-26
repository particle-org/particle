package com.particle.game.utils.blueprint.node;

import com.particle.game.utils.blueprint.context.BackgroundContext;

public class ConditionNode<T extends BackgroundContext> extends BaseTaskNode<T> {

    private Condition<T> condition;
    private BaseTaskNode<T> onPassTask;

    public ConditionNode(String taskName, Condition<T> condition) {
        super(taskName);
        this.condition = condition;
    }

    @Override
    public void run(T context) {
        if (this.condition.test(context)) {
            this.onPassTask.run(context);
        } else {
            this.next(context);
        }
    }

    public void onConditionPassed(BaseTaskNode<T> next) {
        if (this.onPassTask == null) {
            this.onPassTask = next;
        } else {
            BaseTaskNode<T> currentNode = this.onPassTask;
            while (currentNode.getNextNode() != null) {
                currentNode = currentNode.getNextNode();
            }
            currentNode.setNextNode(next);
        }
    }

    @Override
    public void setNextNode(BaseTaskNode<T> next) {
        // 同时设置两个分支的结束节点
        super.setNextNode(next);

        this.onConditionPassed(next);
    }
}
