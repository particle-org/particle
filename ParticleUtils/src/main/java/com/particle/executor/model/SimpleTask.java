package com.particle.executor.model;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTask implements ExecutableTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTask.class);

    private String name;
    private ExecutableTask task;
    private ExecutableCallback callback;
    private Exception exception;
    private TaskState state;

    public SimpleTask(String name, ExecutableTask task) {
        this.name = name;
        this.task = task;
        this.state = TaskState.NEW;
    }

    public SimpleTask(String name, ExecutableTask task, ExecutableCallback callback) {
        this.name = name;
        this.task = task;
        this.callback = callback;
        this.state = TaskState.NEW;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public void setTask(ExecutableTask task) {
        this.task = task;
    }

    public void setCallback(ExecutableCallback callback) {
        this.callback = callback;
    }

    public String getName() {
        return name;
    }

    public ExecutableTask getTask() {
        return task;
    }

    public ExecutableCallback getCallback() {
        return callback;
    }

    public Exception getException() {
        return exception;
    }

    public TaskState getState() {
        return state;
    }

    @Override
    public void run() {
        //标记执行中状态
        this.setState(TaskState.EXECUTING);

        //执行任务
        try {
            ExecutableTask executableTask = this.getTask();
            if (executableTask != null) {
                executableTask.run();
                this.setState(TaskState.EXECUTED);
            }
        } catch (Exception e) {
            LOGGER.error("Task {} run fail", this.getName(), e);

            this.setState(TaskState.ERROR);
            this.setException(e);
        } catch (Error e) {
            LOGGER.error("Task {} run fail", this.getName(), e);

            this.setState(TaskState.ERROR);
            this.setException(new RuntimeException("Error"));
        }

        //执行回调
        try {
            ExecutableCallback callback = this.getCallback();
            if (callback != null) {
                callback.run(this.getException());
            }
        } catch (Exception e) {
            LOGGER.error("Task {} callback fail", this.getName());
            LOGGER.error("Exception: ", e);
        }
    }
}
