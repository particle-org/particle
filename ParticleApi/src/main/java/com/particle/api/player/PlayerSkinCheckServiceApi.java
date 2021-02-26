package com.particle.api.player;

import com.particle.model.entity.model.skin.PlayerSkinData;

import java.util.concurrent.Executor;

public interface PlayerSkinCheckServiceApi {

    /**
     * 检验入口，同步调用即可
     *
     * @param skin
     * @param checkResultCallback
     * @param checkThread
     */
    void checkSkin(PlayerSkinData skin, CheckResultCallback checkResultCallback, Executor checkThread);

    @FunctionalInterface
    interface CheckResultCallback {
        void callback(boolean result);
    }
}
