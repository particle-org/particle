package com.particle.executor.service;

import com.particle.executor.thread.IScheduleThread;
import com.particle.executor.thread.Timer2Thread;
import com.particle.executor.thread.monitor.TimerMonitorThread;

public abstract class AbstractScheduleService {

    private static IScheduleThread[] threadIndex = new IScheduleThread[80];

    public static IScheduleThread getThreadById(long index) {
        return AbstractScheduleService.threadIndex[(int) index];
    }

    protected IScheduleThread createThread(String name) {
        // Plan 1 : 使用Java自带的Schedule
        Timer2Thread scheduleThread = new Timer2Thread(name);

        // Plan EXT : 使用带监控的schedule
        IScheduleThread workThread = new TimerMonitorThread(scheduleThread);

        workThread.execute(() -> {
            if (workThread.getId() >= AbstractScheduleService.threadIndex.length) {
                IScheduleThread[] currentIndex = AbstractScheduleService.threadIndex;

                AbstractScheduleService.threadIndex = new IScheduleThread[currentIndex.length * 2];

                System.arraycopy(currentIndex, 0, AbstractScheduleService.threadIndex, 0, currentIndex.length);
            }

            AbstractScheduleService.threadIndex[(int) scheduleThread.getId()] = scheduleThread;
        });


        return workThread;
    }

    protected void destroyThread(IScheduleThread thread) {
        thread.terminate();

        AbstractScheduleService.threadIndex[(int) thread.getId()] = null;
    }
}
