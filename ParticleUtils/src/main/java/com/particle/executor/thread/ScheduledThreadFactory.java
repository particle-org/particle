package com.particle.executor.thread;

import java.util.concurrent.ThreadFactory;

public class ScheduledThreadFactory implements ThreadFactory {

    private String name;

    public ScheduledThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, name);
    }
}
