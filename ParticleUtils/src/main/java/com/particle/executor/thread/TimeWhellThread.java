package com.particle.executor.thread;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;
import com.particle.executor.model.RealtimeTask;
import com.particle.executor.model.RepeatingTask;
import com.particle.executor.model.SimpleTask;
import com.particle.util.timingwhell.TimerSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class TimeWhellThread implements IScheduleThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeWhellThread.class);

    private TimerSchedule timerSchedule;

    /**
     * 任务数量相关缓存，不直接调用task.size()，提高效率
     */
    private AtomicLong executedAmount = new AtomicLong(0);
    private AtomicLong executingAmount = new AtomicLong(0);
    private AtomicLong submitAmount = new AtomicLong(0);

    private long id = -1;

    private String name;

    public TimeWhellThread(String threadName) {
        this.name = threadName;
        this.timerSchedule = new TimerSchedule(threadName);
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
        this.timerSchedule.shutdown();
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
        return this.timerSchedule.scheduleDelayJob(() -> {
            //统计执行数量
            this.executingAmount.incrementAndGet();

            //执行任务
            new SimpleTask(name, task, callback).run();

            //计数
            this.executingAmount.decrementAndGet();
            this.executedAmount.incrementAndGet();

            return null;
        }, time);
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
        //配置周期任务的信息
        RepeatingTask repeatingTask = new RepeatingTask(name, interval);
        repeatingTask.setLastExecutedTime(System.currentTimeMillis());
        repeatingTask.setTask(() -> {
            //检查任务是否已经取消
            if (repeatingTask.getTask() != null) {

                //配置当前时间
                repeatingTask.setLastExecutedTime(System.currentTimeMillis());

                //执行业务逻辑
                task.run();

                long nextInterval = interval - (System.currentTimeMillis() - repeatingTask.getExecutedTimestamp());

                //若任务没有被取消，则继续执行
                if (!repeatingTask.isCancelled()) {
                    //配置下次执行
                    this.timerSchedule.scheduleDelayJob(() -> {
                        //统计执行数量
                        this.executingAmount.incrementAndGet();

                        //执行任务
                        repeatingTask.run();

                        //计数
                        this.executingAmount.decrementAndGet();
                        this.executedAmount.incrementAndGet();

                        return null;
                    }, nextInterval > 0 ? nextInterval : 0);
                }
            }
        });
        repeatingTask.setCallback((e) -> {
            //如果执行过程发生异常，则打印异常
            if (e != null) {
                LOGGER.error("Task {} run fail", name);
                LOGGER.error("Exception", e);

                //对于不需要keeplive的任务可以跳过
                if (!keeplive) {
                    repeatingTask.cancel(false);
                }
            }
        });

        //创建周期任务
        this.execute(repeatingTask);

        return repeatingTask;
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
        return this.timerSchedule.scheduleJob(() -> {
            //统计执行数量
            this.executingAmount.incrementAndGet();

            //执行任务
            runnable.run();

            //计数
            this.executingAmount.decrementAndGet();
            this.executedAmount.incrementAndGet();

            return null;
        });
    }
}
