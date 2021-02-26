package com.particle.executor.thread;

import com.particle.executor.api.ExecutableCallback;
import com.particle.executor.api.ExecutableTask;
import com.particle.executor.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

public class ScheduleThread extends WorkThread implements IScheduleThread {

    private Logger logger = LoggerFactory.getLogger(ScheduleThread.class);

    private List<DelayTask> delayTasks = Collections.synchronizedList(new LinkedList<>());
    private List<RepeatingTask> repeatingTasks = Collections.synchronizedList(new LinkedList<>());

    public ScheduleThread(String name) {
        super(name);
    }

    @Override
    public void start() {
        this.tick();

        super.start();
    }

    /**
     * 主Tick线程
     * <p>
     * 该线程是服务端task的运行线程
     * 每次tick为服务端一次任务周期
     * tick时间没有最高20限制，只要有空就会运行
     */
    private void tick() {
        //tick的业务逻辑也在tick线程中执行
        super.execute(new NormalTask("TickTask", () -> {
            //拉取需要执行的延迟任务并执行
            List<DelayTask> delayTaskExecutedList = this.getDelayTaskExecutedList();
            delayTaskExecutedList.forEach(super::execute);

            //拉取需要执行的周期任务并执行
            List<RepeatingTask> repeatTaskExecutedList = this.getRepeatTaskExecutedList();
            repeatTaskExecutedList.forEach(super::execute);

            //循环执行tick
            this.tick();
        }));
    }

    @Override
    public Future scheduleRealtimeTask(String name, ExecutableTask task, long ttl) {
        return this.scheduleRealtimeTask(name, task, null, ttl);
    }

    @Override
    public Future scheduleRealtimeTask(String name, ExecutableTask task, ExecutableCallback callback, long ttl) {
        CancellableTask cancellableTask = new CancellableTask(name, new RealtimeTask(name, task, callback, ttl), callback);

        super.execute(cancellableTask);

        return cancellableTask;
    }


    /**
     * 推送单线程任务
     *
     * @param name 任务名称
     * @param task 待执行任务
     * @return 创建的任务
     */
    @Override
    public Future scheduleSimpleTask(String name, ExecutableTask task) {
        return this.scheduleSimpleTask(name, task, null);
    }

    @Override
    public Future scheduleSimpleTask(String name, ExecutableTask task, ExecutableCallback callback) {
        CancellableTask cancellableTask = new CancellableTask(name, task, callback);

        super.execute(cancellableTask);

        return cancellableTask;
    }

    /**
     * 推送延迟任务
     *
     * @param name 任务名称
     * @param task 待执行任务
     * @param time 延迟时间（ms）
     * @return 创建的任务
     */
    @Override
    public Future scheduleDelayTask(String name, ExecutableTask task, long time) {
        return this.scheduleDelayTask(name, task, null, time);
    }

    @Override
    public Future scheduleDelayTask(String name, ExecutableTask task, ExecutableCallback callback, long time) {
        //配置延迟任务信息
        DelayTask delayTask = new DelayTask(name, task, callback, time + System.currentTimeMillis());

        //排期延迟任务
        super.execute(new NormalTask("DelayTaskSchedule", () -> this.scheduleDelayTaskInList(delayTask)));

        return delayTask;
    }

    /**
     * 推送周期任务
     *
     * @param name     任务名称
     * @param task     待执行的任务
     * @param interval 周期时间（ms）
     * @return 创建的任务
     */
    @Override
    public Future scheduleRepeatingTask(String name, ExecutableTask task, long interval) {
        return this.scheduleRepeatingTask(name, task, interval, false);
    }

    @Override
    public Future scheduleRepeatingTask(String name, ExecutableTask task, long interval, boolean keeplive) {
        //配置周期任务的信息
        RepeatingTask repeatingTask = new RepeatingTask(name, interval);
        repeatingTask.setLastExecutedTime(System.currentTimeMillis());
        repeatingTask.setTask(() -> {
            task.run();

            repeatingTask.setLastExecutedTime(System.currentTimeMillis());
        });
        repeatingTask.setCallback((e) -> {
            //如果执行过程发生异常，则打印异常
            if (e != null) {
                logger.error("Task {} run fail", name);
                logger.error("Exception", e);

                //对于不需要keeplive的任务可以跳过
                if (!keeplive) {
                    return;
                }
            }

            //若任务没有被取消，则继续执行
            if (!repeatingTask.isCancelled()) {
                this.scheduleRepeatTaskInList(repeatingTask);
            }
        });

        //排期周期任务
        super.execute(new NormalTask("RepeatingTaskSchedule", () -> this.scheduleRepeatTaskInList(repeatingTask)));

        return repeatingTask;
    }

    /**
     * 查询排期任务的列表
     *
     * @return
     */
    @Override
    public List<String> getScheduledTaskList() {
        List<String> taskNames = new LinkedList<>();

        long timestamp = System.currentTimeMillis();

        new LinkedList<>(delayTasks).forEach(scheduledTask -> {
            String formatData = String.format("%s@%S", scheduledTask.getName(), scheduledTask.getExecutedTimestamp() - timestamp);
            taskNames.add(formatData);
        });

        return taskNames;
    }

    /**
     * 查询周期任务的列表
     *
     * @return
     */
    @Override
    public List<String> getRepeatingTaskList() {
        List<String> taskNames = new LinkedList<>();

        long timestamp = System.currentTimeMillis();

        new LinkedList<>(repeatingTasks).forEach(scheduledTask -> {
            String formatData = String.format("%s@%S", scheduledTask.getName(), scheduledTask.getExecutedTimestamp() - timestamp);
            taskNames.add(formatData);
        });

        return taskNames;
    }

    /**
     * 查询目前队列中的任务数量
     *
     * @return
     */
    @Override
    public long getScheduledTasksAmount() {
        return this.delayTasks.size();
    }

    /**
     * 查询目前队列中周期任务数量
     *
     * @return
     */
    @Override
    public long getRepeatingTasksAmount() {
        return this.repeatingTasks.size();
    }

    /**
     * 将新添加的任务放入处理队列
     *
     * @param delayTask 待处理任务
     */
    private synchronized void scheduleDelayTaskInList(DelayTask delayTask) {
        for (int i = 0; i < this.delayTasks.size(); i++) {
            if (this.delayTasks.get(i).getExecutedTimestamp() > delayTask.getExecutedTimestamp()) {
                this.delayTasks.add(delayTask);

                return;
            }
        }

        this.delayTasks.add(delayTask);
    }

    /**
     * 获取待执行的任务列表
     */
    private synchronized List<DelayTask> getDelayTaskExecutedList() {
        long currentTimestamp = System.currentTimeMillis();

        List<DelayTask> executedList = new LinkedList<>();

        for (DelayTask task : this.delayTasks) {
            if (task.getExecutedTimestamp() < currentTimestamp) {
                executedList.add(task);
            }
        }

        this.delayTasks.removeAll(executedList);

        return executedList;
    }

    /**
     * 将新添加的任务放入处理队列
     *
     * @param repeatingTask 待处理任务
     */
    private synchronized void scheduleRepeatTaskInList(RepeatingTask repeatingTask) {
        for (int i = 0; i < this.repeatingTasks.size(); i++) {
            if (this.repeatingTasks.get(i).getExecutedTimestamp() > repeatingTask.getExecutedTimestamp()) {
                this.repeatingTasks.add(i, repeatingTask);

                return;
            }
        }

        this.repeatingTasks.add(repeatingTask);
    }

    /**
     * 获取待执行的任务列表
     */
    private synchronized List<RepeatingTask> getRepeatTaskExecutedList() {
        long currentTimestamp = System.currentTimeMillis();

        List<RepeatingTask> executedList = new LinkedList<>();

        for (RepeatingTask task : this.repeatingTasks) {
            if (task.getExecutedTimestamp() < currentTimestamp) {
                executedList.add(task);
            }
        }

        this.repeatingTasks.removeAll(executedList);

        return executedList;
    }
}
