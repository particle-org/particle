package com.particle.game.player;

import com.particle.api.entity.IPlayerOnlineTimeRecorderServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.player.components.OnlineTimeRecorderModule;
import com.particle.game.utils.ecs.ECSComponentService;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerOnlineTimeRecorderService implements IPlayerOnlineTimeRecorderServiceApi {

    private static final ECSModuleHandler<OnlineTimeRecorderModule> ONLINE_TIME_RECORDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(OnlineTimeRecorderModule.class);


    @Inject
    private ECSComponentService ecsComponentService;

    /**
     * 记录玩家登录时间
     *
     * @param player
     */
    public void markLoginTime(Player player) {
        OnlineTimeRecorderModule onlineTimeRecorderModule = ONLINE_TIME_RECORDER_MODULE_HANDLER.bindModule(player);
        onlineTimeRecorderModule.setLoginTime(System.currentTimeMillis());
    }

    /**
     * 记录玩家登出时间
     *
     * @param player
     */
    public void markLogoutTime(Player player) {
        OnlineTimeRecorderModule onlineTimeRecorderModule = ONLINE_TIME_RECORDER_MODULE_HANDLER.getModule(player);
        if (onlineTimeRecorderModule == null) {
            return;
        }

        long loginTime = onlineTimeRecorderModule.getLoginTime();

        if (loginTime > 0) {
            long onlineTime = (System.currentTimeMillis() - loginTime) / 1000;

            onlineTimeRecorderModule.setOnlineTime(onlineTimeRecorderModule.getOnlineTime() + onlineTime);
        }
    }

    /**
     * 查询玩家在线时间
     *
     * @param player
     * @return
     */
    @Override
    public long getPlayerOnlineTime(Player player) {
        OnlineTimeRecorderModule onlineTimeRecorderModule = ONLINE_TIME_RECORDER_MODULE_HANDLER.getModule(player);
        if (onlineTimeRecorderModule == null) {
            return 0;
        }

        long loginTime = onlineTimeRecorderModule.getLoginTime();

        if (loginTime > 0) {
            long onlineTime = (System.currentTimeMillis() - loginTime) / 1000;

            return onlineTimeRecorderModule.getOnlineTime() + onlineTime;
        } else {
            return onlineTimeRecorderModule.getOnlineTime();
        }
    }
}
