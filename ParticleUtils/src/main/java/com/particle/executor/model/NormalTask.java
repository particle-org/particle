package com.particle.executor.model;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;

public class NormalTask extends AbstractScheduleTask {
    public NormalTask(String name, ExecutableTask task) {
        super(name, task);
    }

    public NormalTask(String name, ExecutableTask task, ExecutableCallback callback) {
        super(name, task, callback);
    }
}
