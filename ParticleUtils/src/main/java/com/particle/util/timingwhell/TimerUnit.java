package com.particle.util.timingwhell;

import java.util.LinkedList;
import java.util.List;

public class TimerUnit {

    private List<TimerTask> tasks = new LinkedList<>();

    public synchronized void addTask(TimerTask task) {
        tasks.add(task);
    }

    public List<TimerTask> fetchTask() {
        List<TimerTask> tasks = this.tasks;

        this.tasks = new LinkedList<>();

        return tasks;
    }
}
