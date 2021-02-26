package com.particle.util.timingwhell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 基础任务节点
 * 每一个提交的任务都会被封装成任务节点
 * 该节点存储任务的执行状态
 */
public class TimerTask<T> implements Future<T>, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerTask.class);

    private TimerJob<T> job;

    private T result;

    private TimerTaskState state = TimerTaskState.WAITING;

    private long delay = 0;

    public TimerTask(TimerJob<T> job) {
        this.job = job;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // TODO: 2019/8/9 实现interrupt
        return this.updateState(TimerTaskState.CANCELLED);
    }

    @Override
    public boolean isCancelled() {
        return this.state == TimerTaskState.CANCELLED;
    }

    @Override
    public boolean isDone() {
        return this.state == TimerTaskState.DONE;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        while (true) {
            // 如果已经完成，则返回结果
            if (state == TimerTaskState.DONE) {
                return result;
            }

            // 如果未完成，则返回null
            if (state == TimerTaskState.CANCELLED) {
                return null;
            }

            // 等待50ms
            wait(50);
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long waitTimeMiles = unit.toMillis(timeout) + System.currentTimeMillis();

        while (true) {
            // 如果已经完成，则返回结果
            if (state == TimerTaskState.DONE) {
                return result;
            }

            // 如果未完成，则返回null
            if (state == TimerTaskState.CANCELLED) {
                return null;
            }

            // 判断是否超时
            if (waitTimeMiles > System.currentTimeMillis()) {
                return null;
            }

            // 等待50ms
            wait(50);
        }
    }

    @Override
    public void run() {
        // 更新状态
        this.updateState(TimerTaskState.RUNNING);

        if (this.state == TimerTaskState.RUNNING) {
            // 执行业务
            try {
                this.result = this.job.run();
            } catch (Exception e) {
                LOGGER.error("Task Exception.", e);
            } catch (Throwable t) {
                LOGGER.error("Unknown Exception.", t);
            }

            // 标记已完成
            this.updateState(TimerTaskState.DONE);
        }
    }

    /**
     * 切换任务状态
     * WAITING -> RUNNING/CANCELLED
     * RUNNING -> DONE/CANCELLED/RUNNING
     *
     * @param state
     * @return
     */
    protected synchronized boolean updateState(TimerTaskState state) {
        // 判断当前状态
        switch (this.state) {
            case WAITING:
                // WAITING 可以切换到RUNNING或CANCELLED
                if (state == TimerTaskState.RUNNING || state == TimerTaskState.CANCELLED) {
                    this.state = state;
                    return true;
                } else {
                    return false;
                }
            case RUNNING:
                // RUNNING可以切换到除WAITING以外的各种状态
                if (state != TimerTaskState.WAITING) {
                    this.state = state;
                    return true;
                } else {
                    return false;
                }
            case DONE:
            case CANCELLED:
                return false;
        }

        return false;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
