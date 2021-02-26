package com.particle.event.dispatcher;


import com.particle.event.handle.LevelEventHandle;
import com.particle.event.router.LevelEventRouter;
import com.particle.executor.thread.IScheduleThread;
import com.particle.model.events.level.LevelEvent;
import com.particle.model.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicLong;

public class EventDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventDispatcher.class);

    // 记录执行时间过长的事件的阈值 20ms
    private static final long EVENT_LOG_RUN_TIME_THREASHOLD = 20000000;

    private AtomicLong eventCount = new AtomicLong(0);
    private AtomicLong eventRunningTime = new AtomicLong(0);

    /**
     * 单例对象
     */
    private static final EventDispatcher INSTANCE = new EventDispatcher();

    /**
     * 获取单例
     */
    public static EventDispatcher getInstance() {
        return EventDispatcher.INSTANCE;
    }

    private EventDispatcher() {
    }

    public void subscript(LevelEventHandle handle) {
        LevelEventRouter.subscript(handle);
    }

    public void dispatchEvent(LevelEvent event) {
        long nanoTimestamp = System.nanoTime();

        //没有对应的Level时的处理
        Level level = event.getLevel();
        if (level == null) {
            LOGGER.warn("run level event task, the level is null!");

            Runnable onEventNotExecuted = event.getOnEventNotExecuted();
            if (onEventNotExecuted != null) {
                onEventNotExecuted.run();
            }

            Runnable onEventCancelled = event.getOnEventCancelled();
            if (onEventCancelled != null) {
                onEventCancelled.run();
            }
            return;
        }

        //路由
        List<LevelEventHandle> eventHandles = LevelEventRouter.route(event);

        //没有对应的Handle时的处理
        if (eventHandles == null) {
            Runnable onEventNotExecuted = event.getOnEventNotExecuted();
            if (onEventNotExecuted != null) {
                onEventNotExecuted.run();
            }

            Runnable afterEventExecuted = event.getAfterEventExecuted();
            if (afterEventExecuted != null) {
                afterEventExecuted.run();
            }

            return;
        }

        //执行任务
        IScheduleThread levelSchedule = event.getLevel().getLevelSchedule();

        //若当前线程是Level线程，则直接执行
        if (Thread.currentThread().getId() == levelSchedule.getId()) {
            this.runningEvent(level, event, eventHandles);
        } else {
            //若当前线程不是Level线程，则进入调度逻辑
            try {
                levelSchedule.scheduleSimpleTask(
                        "level_event_handle" + event.getClass().getSimpleName(),
                        () -> this.runningEvent(level, event, eventHandles)
                );
            } catch (RejectedExecutionException e) {
                // 如果事件无法提交，则主动cancel，但是由于不确定运行线程是否合法，不执行回调
                event.cancel();
                LOGGER.error("Fail to dispatch event {} because thread shutdown!", event.getClass().getSimpleName());
            }
        }

        // 统计
        this.eventCount.incrementAndGet();

        long timeRunning = System.nanoTime() - nanoTimestamp;
        this.eventRunningTime.addAndGet(timeRunning);
        if (timeRunning > EVENT_LOG_RUN_TIME_THREASHOLD) {
            LOGGER.warn("Event {} running tooooo long! cost {}ms", event.getClass().getName(), timeRunning / 1000000);
        }
    }

    /**
     * 针对流处理模式的事件发送器
     *
     * @param event
     * @param object
     * @param onHandled
     * @param onCancelled
     * @param <T>
     */
    public <T> void dispatchEvent(LevelEvent event, T object, Callback<T> onHandled, Callback<T> onCancelled) {
        long nanoTimestamp = System.nanoTime();

        event.overrideOnEventCancelled(() -> onCancelled.run(object));
        event.overrideAfterEventExecuted(() -> onHandled.run(object));

        //执行任务
        this.dispatchEvent(event);

        // 统计
        this.eventCount.incrementAndGet();

        long timeRunning = System.nanoTime() - nanoTimestamp;
        this.eventRunningTime.addAndGet(timeRunning);
        if (timeRunning > EVENT_LOG_RUN_TIME_THREASHOLD) {
            LOGGER.warn("Event {} running tooooo long! cost {}ms", event.getClass().getName(), timeRunning / 1000000);
        }
    }

    private void runningEvent(Level level, LevelEvent event, List<LevelEventHandle> eventHandles) {
        boolean isRunCancel = false;
        try {
            // 执行处理业务
            for (LevelEventHandle eventHandle : eventHandles) {
                // 若event被cancel则停止后面的执行
                if (event.isCancelled()) {
                    Runnable onEventCancelled = event.getOnEventCancelled();
                    if (onEventCancelled != null) {
                        onEventCancelled.run();
                    }
                    isRunCancel = true;
                    break;
                }
                eventHandle.handle(level, event);
            }
        } catch (Exception e) {
            LOGGER.error("Event {} handle error", event.getClass().getName());
            LOGGER.error("Exception: ", e);

            //若检测到异常，则取消Event
            Runnable onEventCancelled = event.getOnEventCancelled();
            if (onEventCancelled != null) {
                onEventCancelled.run();
            }
            isRunCancel = true;
        } catch (Error e) {
            LOGGER.error("Event {} handle error", event.getClass().getName());
            LOGGER.error("Error: ", e);

            //若检测到异常，则取消Event
            Runnable onEventCancelled = event.getOnEventCancelled();
            if (onEventCancelled != null) {
                onEventCancelled.run();
            }
        }

        //若没有异常，则直接执行回调
        if (!event.isCancelled()) {
            Runnable afterEventExecuted = event.getAfterEventExecuted();
            if (afterEventExecuted != null) {
                afterEventExecuted.run();
            }
        } else if (!isRunCancel) {
            Runnable onEventCancelled = event.getOnEventCancelled();
            if (onEventCancelled != null) {
                onEventCancelled.run();
            }
        }
    }

    public long getEventAmount() {
        return this.eventCount.get();
    }

    public long getEventRunningTime() {
        return this.eventRunningTime.get();
    }

    @FunctionalInterface
    public interface Callback<T> {
        void run(T object);
    }
}
