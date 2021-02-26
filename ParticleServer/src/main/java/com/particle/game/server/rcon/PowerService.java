package com.particle.game.server.rcon;

import com.particle.api.server.IPowerServiceApi;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.executor.service.LevelScheduleService;
import com.particle.game.server.Server;
import com.particle.game.ui.TextService;
import com.particle.game.world.level.WorldService;
import com.particle.model.events.level.ShutDownEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PowerService implements IPowerServiceApi {

    private static final Logger logger = LoggerFactory.getLogger(PowerService.class);

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private WorldService worldService;

    @Inject
    private Server server;

    @Inject
    private TextService textService;


    /**
     * 直接关闭
     */
    @Override
    public void shutdown() {
        System.exit(0);
    }

    /**
     * 软关闭
     */
    @Override
    public void softShutdown() {
        logger.info("soft shut down the process");
        server.pause();
        Thread monitorThread = new Thread(() -> {
            long monitorTime = System.currentTimeMillis();
            while (true) {
                try {
                    Thread.sleep(2000);
                    long duration = System.currentTimeMillis() - monitorTime;

                    logger.warn(String.format("receive the soft shutdown order, pass [%s]", duration));
                    if (duration > 60 * 1000) {
                        logger.error("收到softShutdown指令，监控强制退出");
                        System.exit(0);
                    }

                    printThreads();
                } catch (Exception e) {
                    logger.error("soft_shutdown_monitor_thread exception!", e);
                }
            }
        });
        monitorThread.setName("soft_shutdown_monitor_thread");
        monitorThread.setDaemon(true);
        ShutDownEvent shutDownEvent = new ShutDownEvent(this.worldService.getDefaultLevel());
        shutDownEvent.overrideOnEventCancelled(() -> {
            logger.warn("取消了关闭事件");
            server.restart();
        });
        shutDownEvent.overrideAfterEventExecuted(() -> {
            LevelScheduleService.getInstance().getDefaultThread().scheduleSimpleTask(
                    "shutdown_task",
                    () -> server.shutdown()
            );
        });
        eventDispatcher.dispatchEvent(shutDownEvent);
        monitorThread.start();
    }

    private void printThreads() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // 遍历线程组树，获取根线程组
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // 激活的线程数加倍
        int estimatedSize = topGroup.activeCount() * 2;
        Thread[] slackList = new Thread[estimatedSize];
        // 获取根线程组的所有线程
        int actualSize = topGroup.enumerate(slackList);
        // copy into a list that is the exact size
        Thread[] list = new Thread[actualSize];
        System.arraycopy(slackList, 0, list, 0, actualSize);

        logger.info("等待以下线程退出： ");
        for (Thread thread : list) {
            if (!thread.isDaemon()) {
                logger.info(thread.getName());
            }
        }
    }
}
