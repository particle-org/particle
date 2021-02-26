package com.particle.game.ui.task;

import com.particle.executor.service.LevelScheduleService;
import com.particle.game.server.Server;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Singleton
public class UITaskHandler {

    @Inject
    private Server server;

    /**
     * 存储临时的列表
     */
    private LinkedList<ITask> taskQueue = new LinkedList<>();

    private Map<Player, ITask> popupTask = new ConcurrentHashMap<>();
    private Map<Player, ITask> systemTask = new ConcurrentHashMap<>();
    private Map<Player, ITask> tipTask = new ConcurrentHashMap<>();
    private Map<Player, ITask> soundTask = new ConcurrentHashMap<>();
    private Map<Player, ITask> actionBarTask = new ConcurrentHashMap<>();


    /**
     * 开始循环监听
     */
    @Inject
    public void start() {
        LevelScheduleService.getInstance().getDefaultThread().scheduleRepeatingTask("UITaskHandler", () -> {
            UITaskHandler.this.onRun();
        }, 50);
    }

    /**
     * 增加新的任务到队列中
     *
     * @param task
     */
    public void addTaskQueue(ITask task) {
        synchronized (this.taskQueue) {
            this.taskQueue.offer(task);
        }
    }

    /**
     * 开始运行
     */
    private void onRun() {
        this.handleTaskQueue();
        tickProcess(popupTask);
        tickProcess(systemTask);
        tickProcess(soundTask);
        tickProcess(tipTask);
        tickProcess(actionBarTask);
    }

    private void handleTaskQueue() {
        Set<ITask> cancelTask = null;
        synchronized (this.taskQueue) {//尽量减少同步块内的操作，减少竞争
            while (!this.taskQueue.isEmpty()) {
                ITask replaceTask = this.addTask(this.taskQueue.poll());
                if (replaceTask != null) {
                    if (cancelTask == null) {
                        cancelTask = new HashSet<>();
                    }
                    cancelTask.add(replaceTask);
                }
            }
        }
        if (cancelTask != null) {
            for (ITask task : cancelTask) {
                task.cancel();
            }
        }
    }

    private void tickProcess(Map<Player, ITask> tasks) {
        Iterator<Map.Entry<Player, ITask>> itr = tasks.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Player, ITask> entry = itr.next();
            ITask task = entry.getValue();
            boolean exception = false;
            try {
                task.onRun();
            } catch (Exception e) {
                exception = true;//发生异常时直接剔除该任务
                e.printStackTrace();
            }
            if (!server.isOnline(entry.getKey())) {
                itr.remove();
            } else if (exception || !task.isValid()) {
                itr.remove();
            }
        }
    }

    private synchronized ITask addTask(ITask task) {
        switch (task.getTaskType()) {//switch枚举优化性能
            case POPUP_KEEP:
                return this.popupTask.put(task.getHolder(), task);
            case SYSTEM_KEEP:
                return this.systemTask.put(task.getHolder(), task);
            case TIP_KEEP:
                return this.tipTask.put(task.getHolder(), task);
            case SOUND_PLAYER:
                return this.soundTask.put(task.getHolder(), task);
            case ACTIONBAR:
                return this.actionBarTask.put(task.getHolder(), task);
        }
        return null;
    }


}
