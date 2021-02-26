package com.particle.game.utils.blueprint.node;

import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.utils.blueprint.context.BackgroundContext;

public class EventNode<T extends BackgroundContext> extends BaseTaskNode<T> {

    private EventCreater<T> eventCreater;

    private BaseTaskNode<T> handleNode;
    private BaseTaskNode<T> cancelNode;

    public EventNode(String taskName, EventCreater<T> eventCreater) {
        super(taskName);
        this.eventCreater = eventCreater;
    }

    @Override
    public void setNextNode(BaseTaskNode<T> next) {
        // 同时设置所有分支的结束节点
        super.setNextNode(next);

        this.onEventHandled(next);
        this.onEventCancel(next);
    }

    public void onEventHandled(BaseTaskNode<T> next) {
        if (this.handleNode == null) {
            this.handleNode = next;
        } else {
            BaseTaskNode<T> currentNode = this.handleNode;
            while (currentNode.getNextNode() != null) {
                currentNode = currentNode.getNextNode();
            }
            currentNode.setNextNode(next);
        }
    }

    public void onEventCancel(BaseTaskNode<T> next) {
        if (this.cancelNode == null) {
            this.cancelNode = next;
        } else {
            BaseTaskNode<T> currentNode = this.cancelNode;
            while (currentNode.getNextNode() != null) {
                currentNode = currentNode.getNextNode();
            }
            currentNode.setNextNode(next);
        }
    }

    @Override
    public void run(T context) {
        EventDispatcher.getInstance().dispatchEvent(
                this.eventCreater.create(context), context,
                (ctx) -> this.handleNode.run(ctx),
                (ctx) -> this.cancelNode.run(ctx)
        );
    }
}
