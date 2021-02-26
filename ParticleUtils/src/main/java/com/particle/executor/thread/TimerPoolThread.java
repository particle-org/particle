package com.particle.executor.thread;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;
import com.particle.executor.model.RealtimeTask;
import com.particle.executor.model.RepeatingTask;
import com.particle.executor.model.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class TimerPoolThread implements IScheduleThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(Timer2Thread.class);

    private ScheduledExecutorService scheduledExecutorService;
    private BlockingQueue<Runnable>[] executorsTaskQueue;
    private ExecutorService[] executors;

    /**
     * 任务数量相关缓存，不直接调用task.size()，提高效率
     */
    private AtomicLong executedAmount = new AtomicLong(0);
    private AtomicLong executingAmount = new AtomicLong(0);
    private AtomicLong submitAmount = new AtomicLong(0);

    private String name;
    private long id = -1;

    public TimerPoolThread(String threadName) {
        this.name = threadName;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ScheduledThreadFactory(threadName + "_schedule"));
        this.scheduledExecutorService.submit(() -> {
            this.id = Thread.currentThread().getId();
        });

        // 因为开发时间不足先用Reactor模型做，以后考虑CachedThreadPool优化
        this.executors = new ExecutorService[4];
        this.executorsTaskQueue = new LinkedBlockingQueue[4];
        for (int i = 0; i < 4; i++) {
            this.executorsTaskQueue[i] = new LinkedBlockingQueue<Runnable>();
            this.executors[i] = new ThreadPoolExecutor(
                    1,
                    1,
                    0L,
                    TimeUnit.MILLISECONDS,
                    this.executorsTaskQueue[i],
                    new ScheduledThreadFactory(threadName + "_" + i));
        }
    }

    @Override
    public Future scheduleRealtimeTask(String name, ExecutableTask task, ExecutableCallback callback, long ttl) {
        long min = Integer.MAX_VALUE;
        int selected = -1;

        for (int i = 0; i < this.executorsTaskQueue.length; i++) {
            long executingTaskAmount = this.executorsTaskQueue[i].size();
            if (executingTaskAmount < min) {
                min = executingTaskAmount;
                selected = i;
            }
        }

        return this.executeTask(new RealtimeTask(name, task, callback, ttl), this.executors[selected]);
    }

    @Override
    public Future scheduleSimpleTask(String name, ExecutableTask task, ExecutableCallback callback) {
        long min = Integer.MAX_VALUE;
        int selected = -1;

        for (int i = 0; i < this.executorsTaskQueue.length; i++) {
            long executingTaskAmount = this.executorsTaskQueue[i].size();
            if (executingTaskAmount < min) {
                min = executingTaskAmount;
                selected = i;
            }
        }

        return this.executeTask(new SimpleTask(name, task, callback), this.executors[selected]);
    }

    @Override
    public Future scheduleDelayTask(String name, ExecutableTask task, ExecutableCallback callback, long time) {
        //配置延迟任务信息
        return this.scheduledExecutorService.schedule(() -> this.scheduleSimpleTask(name, task, callback), time, TimeUnit.MILLISECONDS);
    }

    @Override
    public Future scheduleSerialSimpleTask(String name, String identified, ExecutableTask task) {
        int hash = 0;
        for (byte aByte : identified.getBytes()) {
            hash += aByte;
        }

        return this.executeTask(new SimpleTask(name, task, null), this.executors[hash & 3]);
    }

    @Override
    public Future scheduleRepeatingTask(String name, ExecutableTask task, long interval, boolean keeplive) {
        //配置周期任务的信息
        RepeatingTask repeatingTask = new RepeatingTask(name, interval);
        repeatingTask.setLastExecutedTime(System.currentTimeMillis());
        repeatingTask.setTask(() -> {
            //检查任务是否已经取消
            if (repeatingTask.getTask() != null && !repeatingTask.isCancelled()) {
                //配置当前时间
                repeatingTask.setLastExecutedTime(System.currentTimeMillis());

                // 执行任务
                task.run();

                //配置下次执行
                this.scheduledExecutorService.schedule(() -> {
                    this.executeRepeatTask(repeatingTask);
                }, interval, TimeUnit.MILLISECONDS);
            }
        });
        repeatingTask.setCallback((e) -> {
            //如果执行过程发生异常，对于不需要keeplive的任务可以跳过
            if (e != null && !keeplive) {
                repeatingTask.cancel(false);
            }
        });

        // 执行任务
        this.executeRepeatTask(repeatingTask);

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
        throw new RuntimeException("Depressed");
    }

    @Override
    public void execute(Runnable runnable) {
        long min = Integer.MAX_VALUE;
        int selected = -1;

        for (int i = 0; i < this.executorsTaskQueue.length; i++) {
            long executingTaskAmount = this.executorsTaskQueue[i].size();
            if (executingTaskAmount < min) {
                min = executingTaskAmount;
                selected = i;
            }
        }

        this.executeTask(runnable, this.executors[selected]);
    }

    @Override
    public void terminate() {
        for (ExecutorService executor : this.executors) {
            executor.shutdown();
        }
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

    private Future executeRepeatTask(RepeatingTask repeatingTask) {
        long min = Integer.MAX_VALUE;
        int selected = -1;

        for (int i = 0; i < this.executorsTaskQueue.length; i++) {
            long executingTaskAmount = this.executorsTaskQueue[i].size();
            if (executingTaskAmount < min) {
                min = executingTaskAmount;
                selected = i;
            }
        }

        return this.executeTask(repeatingTask, this.executors[selected]);
    }

    private Future executeTask(Runnable runnable, ExecutorService executor) {
        return executor.submit(() -> {
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
