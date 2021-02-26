package com.particle.monitor;

import com.particle.api.ServerApi;
import com.particle.game.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MonitorLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorLoader.class);

    @Inject
    private Server server;

    /**
     * 启动监控
     */
    public void enableMonitor() {
        Thread serverStopMonitor = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error("Monitor thread error!", e);
                }

                if (this.server.getStatus() == ServerApi.ServerStatus.STOP) {
                    this.shutdownMonitor();
                    return;
                }
            }

        });
        serverStopMonitor.setName("LoadMonitor");
        serverStopMonitor.start();
    }

    /**
     * 停止监控
     */
    public void shutdownMonitor() {
    }
}
