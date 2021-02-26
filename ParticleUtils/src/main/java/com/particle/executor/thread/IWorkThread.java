package com.particle.executor.thread;

public interface IWorkThread {
    long getId();

    String getName();

    @Deprecated
    void setName(String name);

    void execute(Runnable runnable);

    void terminate();

    long getExecutingTaskAmount();

    long getExecutedTaskAmount();

    long getSubmitedTaskAmount();
}
