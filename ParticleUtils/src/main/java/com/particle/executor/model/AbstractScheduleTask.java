package com.particle.executor.model;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;

public abstract class AbstractScheduleTask extends SimpleTask {
    public AbstractScheduleTask(String name, ExecutableTask task) {
        super(name, task);
    }

    public AbstractScheduleTask(String name, ExecutableTask task, ExecutableCallback callback) {
        super(name, task, callback);
    }
}
