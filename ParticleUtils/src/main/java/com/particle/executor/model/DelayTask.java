package com.particle.executor.model;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;

public class DelayTask extends CancellableTask {
    private long executedTimestamp;

    public DelayTask(String name, ExecutableTask task, long executedTimestamp) {
        super(name, task);
        this.executedTimestamp = executedTimestamp;
    }

    public DelayTask(String name, ExecutableTask task, ExecutableCallback callback, long executedTimestamp) {
        super(name, task, callback);
        this.executedTimestamp = executedTimestamp;
    }

    public long getExecutedTimestamp() {
        return executedTimestamp;
    }
}
