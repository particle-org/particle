package com.particle.executor.thread;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;
import com.particle.executor.model.RealtimeTask;
import com.particle.executor.model.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Timer2Thread implements IScheduleThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(Timer2Thread.class);

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 任务数量相关缓存，不直接调用task.size()，提高效率
     */
    private AtomicLong executedAmount = new AtomicLong(0);
    private AtomicLong executingAmount = new AtomicLong(0);
    private AtomicLong submitAmount = new AtomicLong(0);

    private long id = -1;

    private String name;

    public Timer2Thread(String threadName) {
        this.name = threadName;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ScheduledThreadFactory(threadName));
        this.scheduledExecutorService.submit(() -> {
            this.id = Thread.currentThread().getId();
        });
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    @Deprecated
    public void setName(String name) {
        this.name = name;

        throw new RuntimeException("Depressed");
    }

    @Override
    public void execute(Runnable task) {
        this.executeTask(task);
    }

    @Override
    public void terminate() {
        this.scheduledExecutorService.shutdown();
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

    @Override
    public Future scheduleRealtimeTask(String name, ExecutableTask task, long ttl) {
        return this.scheduleRealtimeTask(name, task, null, ttl);
    }

    @Override
    public Future scheduleRealtimeTask(String name, ExecutableTask task, ExecutableCallback callback, long ttl) {
        return this.executeTask(new RealtimeTask(name, task, callback, ttl));
    }

    @Override
    public Future scheduleSimpleTask(String name, ExecutableTask task) {
        return this.scheduleSimpleTask(name, task, null);
    }

    @Override
    public Future scheduleSimpleTask(String name, ExecutableTask task, ExecutableCallback callback) {
        return this.executeTask(new SimpleTask(name, task, callback));
    }

    @Override
    public Future scheduleDelayTask(String name, ExecutableTask task, long time) {
        return this.scheduleDelayTask(name, task, null, time);
    }

    @Override
    public Future scheduleDelayTask(String name, ExecutableTask task, ExecutableCallback callback, long time) {
        //配置延迟任务信息
        return this.scheduledExecutorService.schedule(() -> {
            //统计执行数量
            this.executingAmount.incrementAndGet();

            //执行任务
            new SimpleTask(name, task, callback).run();

            //计数
            this.executingAmount.decrementAndGet();
            this.executedAmount.incrementAndGet();
        }, time, TimeUnit.MILLISECONDS);
    }

    @Override
    public Future scheduleRepeatingTask(String name, ExecutableTask task, long interval) {
        return this.scheduleRepeatingTask(name, task, interval, false);
    }

    /**
     * 执行周期任务
     *
     * @param task
     * @param name
     * @param interval
     * @param keeplive
     * @return
     */
    @Override
    public Future scheduleRepeatingTask(String name, ExecutableTask task, long interval, boolean keeplive) {
        //配置延迟任务信息
        return this.scheduledExecutorService.scheduleWithFixedDelay(() -> {
            //统计执行数量
            this.executingAmount.incrementAndGet();

            try {
                task.run();
            } catch (Exception e) {
                LOGGER.error("Task {} run fail", name);
                LOGGER.error("Exception", e);

                //中断任务执行
                // TODO: 2019/1/16 测试效率
                if (!keeplive) {
                    throw e;
                }
            } catch (Error e) {
                LOGGER.error("Task {} run fail", name);
                LOGGER.error("Error", e);

                //中断任务执行
                // TODO: 2019/1/16 测试效率
                if (!keeplive) {
                    throw e;
                }
            } finally {
                //计数
                this.executingAmount.decrementAndGet();
                this.executedAmount.incrementAndGet();
            }
        }, 0, interval, TimeUnit.MILLISECONDS);
    }

    @Override
    public List<String> getScheduledTaskList() {
        return new LinkedList<>();
    }

    @Override
    public List<String> getRepeatingTaskList() {
        return new LinkedList<>();
    }

    @Override
    public long getScheduledTasksAmount() {
        return 0;
    }

    @Override
    public long getRepeatingTasksAmount() {
        return 0;
    }

    private Future executeTask(Runnable runnable) {
        return this.scheduledExecutorService.submit(() -> {
            //统计执行数量
            this.executingAmount.incrementAndGet();

            //执行任务
            try {
                runnable.run();
            } catch (Exception e) {
                LOGGER.error("Task {} run fail", name);
                LOGGER.error("Exception", e);
            } catch (Error e) {
                LOGGER.error("Task {} run fail", name);
                LOGGER.error("Error", e);
            }

            //计数
            this.executingAmount.decrementAndGet();
            this.executedAmount.incrementAndGet();
        });
    }
}
