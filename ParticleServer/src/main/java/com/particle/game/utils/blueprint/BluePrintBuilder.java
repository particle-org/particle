package com.particle.game.utils.blueprint;

import com.particle.game.utils.blueprint.context.BackgroundContext;
import com.particle.game.utils.blueprint.node.*;
import com.particle.game.utils.blueprint.task.JobTask;

public class BluePrintBuilder<T extends BackgroundContext> {

    private String name;
    private RootNode<T> root;
    private BaseTaskNode<T> current;

    private BluePrintBuilder<T> loopBuilder;

    /**
     * 通过名称创建Builder，对外使用
     *
     * @param name 构造器名称
     */
    public BluePrintBuilder(String name) {
        this.name = name;

        this.current = this.root = new RootNode<>(name);
    }

    /**
     * 切换到Level线程
     *
     * @return 构造器
     */
    public BluePrintBuilder<T> level() {
        // 如果是递归状态，则继续迭代
        if (this.loopBuilder != null) {
            this.loopBuilder.level();

            return this;
        }

        // 切换节点
        ScheduleNode<T> switchNode = new LevelScheduleNode<>(name);
        this.current.setNextNode(switchNode);
        this.current = switchNode;

        return this;
    }

    /**
     * 切换到Node线程
     *
     * @return 构造器
     */
    public BluePrintBuilder<T> node() {
        // 如果是递归状态，则继续迭代
        if (this.loopBuilder != null) {
            this.loopBuilder.node();

            return this;
        }

        // 切换节点
        ScheduleNode<T> switchNode = new NodeScheduleNode<>(name);
        this.current.setNextNode(switchNode);
        this.current = switchNode;

        return this;
    }

    /**
     * 创建任务节点
     *
     * @param job 待执行的任务
     * @return 构造器
     */
    public BluePrintBuilder<T> job(JobTask<T> job) {
        // 如果是递归状态，则继续迭代
        if (this.loopBuilder != null) {
            this.loopBuilder.job(job);

            return this;
        }

        //配置任务
        JobNode<T> next = new JobNode<>(name, job);
        this.current.setNextNode(next);
        this.current = next;

        return this;
    }

    /**
     * 创建判断任务
     *
     * @param condition 判断条件
     * @return 构造器
     */
    public BluePrintBuilder<T> check(Condition<T> condition) {
        // 如果是递归状态，则继续迭代
        if (this.loopBuilder != null) {
            this.loopBuilder.check(condition);

            return this;
        }

        // 开启子流
        enableSubStream();

        ConditionNode<T> conditionNode = new ConditionNode<>(name, condition);

        this.current.setNextNode(conditionNode);
        this.current = conditionNode;

        return this;
    }

    /**
     * 关闭判断任务
     *
     * @return 构造器
     */
    public BluePrintBuilder<T> checked() {
        // 检查是否在检查闭环中
        if (this.loopBuilder != null) {

            // 检查自循环是否需要迭代
            if (this.loopBuilder.isLooping()) {
                this.loopBuilder.checked();
            } else {
                // 节点合法性检查
                if (!(this.current instanceof ConditionNode)) {
                    throw new RuntimeException("Checked should append to condition node!");
                }

                // 串接处理流
                BaseTaskNode<T> startNode = this.loopBuilder.build().getNextNode();

                // 配置起始节点
                if (startNode != null) {
                    ((ConditionNode<T>) this.current).onConditionPassed(startNode);
                }

                // 关闭
                this.loopBuilder = null;
            }
        } else {
            throw new RuntimeException("Blue print not in condition mode!");
        }

        return this;
    }

    /**
     * 创建事件任务
     *
     * @param creater 事件创造器
     * @return 构造器
     */
    public BluePrintBuilder<T> event(EventCreater<T> creater) {
        // 如果是递归状态，则继续迭代
        if (this.loopBuilder != null) {
            this.loopBuilder.event(creater);

            return this;
        }

        // 开启子流
        enableSubStream();

        // 创建节点
        EventNode<T> eventNode = new EventNode<>(name, creater);

        this.current.setNextNode(eventNode);
        this.current = eventNode;

        return this;
    }

    /**
     * 构造取消链
     *
     * @return 构造器
     */
    public BluePrintBuilder<T> onCancel() {
        // 检查是否在检查闭环中
        if (this.loopBuilder != null) {

            // 检查自循环是否需要迭代
            if (this.loopBuilder.isLooping()) {
                this.loopBuilder.onCancel();
            } else {
                // 节点合法性检查
                if (!(this.current instanceof EventNode)) {
                    throw new RuntimeException("Checked should append to event node!");
                }

                // 串接处理流
                BaseTaskNode<T> startNode = this.loopBuilder.build().getNextNode();

                // 配置起始节点
                if (startNode != null) {
                    ((EventNode<T>) this.current).onEventCancel(startNode);
                }

                // 重置处理流
                this.loopBuilder = null;
                this.enableSubStream();
            }
        } else {
            throw new RuntimeException("Blue print not int event mode!");
        }

        return this;
    }

    /**
     * 构造处理链
     *
     * @return 构造器
     */
    public BluePrintBuilder<T> onHandle() {
        // 检查是否在检查闭环中
        if (this.loopBuilder != null) {

            // 检查自循环是否需要迭代
            if (this.loopBuilder.isLooping()) {
                this.loopBuilder.onHandle();
            } else {
                // 节点合法性检查
                if (!(this.current instanceof EventNode)) {
                    throw new RuntimeException("Checked should append to event node!");
                }

                // 串接处理流
                BaseTaskNode<T> startNode = this.loopBuilder.build().getNextNode();

                // 配置起始节点
                if (startNode != null) {
                    ((EventNode<T>) this.current).onEventHandled(startNode);
                }

                // 重置处理流
                this.loopBuilder = null;
                this.enableSubStream();
            }
        } else {
            throw new RuntimeException("Blue print not int event mode!");
        }

        return this;
    }

    /**
     * 发送事件
     *
     * @return 构造器
     */
    public BluePrintBuilder<T> dispatcher() {
        // 检查是否在检查闭环中
        if (this.loopBuilder != null) {

            // 检查自循环是否需要迭代
            if (this.loopBuilder.isLooping()) {
                this.loopBuilder.dispatcher();
            } else {
                // 节点合法性检查
                if (!(this.current instanceof EventNode)) {
                    throw new RuntimeException("Checked should append to event node!");
                }

                // 重置处理流
                this.loopBuilder = null;
            }
        } else {
            throw new RuntimeException("Blue print not int event mode!");
        }

        return this;
    }

    /**
     * 构造处理链
     *
     * @return 构造器
     */
    public RootNode<T> build() {
        return this.root;
    }

    /**
     * 当前蓝图是否处于迭代过程中
     *
     * @return 迭代状态
     */
    private boolean isLooping() {
        return loopBuilder != null;
    }

    /**
     * 开启迭代状态
     */
    private void enableSubStream() {
        if (this.loopBuilder == null) {
            this.loopBuilder = new BluePrintBuilder<>(name);
        } else {
            throw new RuntimeException("Sub stream has been enabled");
        }
    }
}
