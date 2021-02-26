package com.particle.executor.api;

@FunctionalInterface
public interface ExecutableCallback {

    void run(Exception exception);

}
