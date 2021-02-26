package com.particle.executor.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class WorkThread extends Thread implements IWorkThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkThread.class);

    private boolean isRunning;

    private ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    /**
     * 任务数量相关缓存，不直接调用task.size()，提高效率
     */
    private AtomicLong executedAmount = new AtomicLong(0);
    private AtomicLong executingAmount = new AtomicLong(0);
    private AtomicLong submitAmount = new AtomicLong(0);

    public WorkThread(String name) {
        this.isRunning = false;

        this.setName(name);
    }

    @Override
    public void execute(Runnable runnable) {
        this.submitAmount.incrementAndGet();

        this.tasks.offer(runnable);
    }

    @Override
    public void terminate() {
        this.isRunning = false;
    }

    @Override
    public void run() {
        //如果已经运行，则停止
        if (this.isRunning) {
            throw new RuntimeException("Thread already running!");
        }

        //启动任务
        this.isRunning = true;
        while (this.isRunning) {
            //从队列中提取一个任务
            // TODO: 2019/1/14 测试BlockingQueue？
            Runnable task = null;
            task = this.tasks.poll();

            if (task != null) {
                //统计执行数量
                this.executingAmount.incrementAndGet();

                //执行任务
                task.run();

                //计数
                this.executingAmount.decrementAndGet();
                this.executedAmount.incrementAndGet();
            }

            //降低CPU占用率
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException e) {
                LOGGER.error("Thread sleep interrupted");
            }
        }
    }

    @Override
    public long getExecutingTaskAmount() {
        return this.executingAmount.get();
    }

    @Override
    public long getExecutedTaskAmount() {
        return this.executedAmount.get();
    }

    @Override
    public long getSubmitedTaskAmount() {
        return this.submitAmount.get();
    }
}
