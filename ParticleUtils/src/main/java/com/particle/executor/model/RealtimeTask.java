package com.particle.executor.model;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealtimeTask extends SimpleTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeTask.class);

    private long timestamp;
    private long ttl;

    public RealtimeTask(String name, ExecutableTask task, long ttl) {
        super(name, task);
        this.ttl = ttl;
        this.timestamp = System.currentTimeMillis();
    }

    public RealtimeTask(String name, ExecutableTask task, ExecutableCallback callback, long ttl) {
        super(name, task, callback);
        this.ttl = ttl;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - this.timestamp < this.ttl) {
            super.run();
        } else {
            LOGGER.debug("Remove task {} because of expired", this.getName());
        }
    }
}
