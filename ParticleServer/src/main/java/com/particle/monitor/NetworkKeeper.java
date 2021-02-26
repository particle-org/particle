package com.particle.monitor;

import com.particle.game.player.PlayerService;
import com.particle.game.server.Server;
import com.particle.game.server.rcon.PowerService;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NetworkKeeper {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkKeeper.class);

    @Inject
    private NetworkManager networkManager;

    @Inject
    private PowerService powerService;

    @Inject
    private Server server;

    @Inject
    private PlayerService playerService;


    public void start() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (this.networkManager.isNetworkInAccident()) {

                    LOGGER.warn("Shutdown server because of network manager error");

                    // 10秒后关服
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // 发送玩家离开
                    // TODO: 2019/7/18 这部分操作应该有部分玩法无法收到
                    for (Player player : this.server.getAllPlayers()) {
                        this.playerService.transfer(player, "42.186.53.31", (short) 19132);
                    }

                    // 启动关服任务
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.powerService.softShutdown();

                    return;
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.setDaemon(true);

        thread.start();
    }

}
