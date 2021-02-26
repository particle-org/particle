package com.particle.executor.model;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CancellableTask extends AbstractScheduleTask implements Future {
    private boolean isCancelled = false;

    public CancellableTask(String name, ExecutableTask task) {
        super(name, task);
    }

    public CancellableTask(String name, ExecutableTask task, ExecutableCallback callback) {
        super(name, task, callback);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        this.setTask(null);

        this.isCancelled = true;

        return this.getState() == TaskState.NEW;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public boolean isDone() {
        return this.getState() == TaskState.EXECUTED || this.getState() == TaskState.ERROR;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
