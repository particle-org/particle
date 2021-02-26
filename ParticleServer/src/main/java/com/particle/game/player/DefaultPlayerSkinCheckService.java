package com.particle.game.player;

import com.google.inject.Singleton;
import com.particle.api.inject.RequestStaticInject;
import com.particle.api.player.PlayerSkinCheckServiceApi;
import com.particle.model.entity.model.skin.PlayerSkinData;

import java.util.concurrent.Executor;

@Singleton
@RequestStaticInject
public class DefaultPlayerSkinCheckService implements PlayerSkinCheckServiceApi {
    @Override
    public void checkSkin(PlayerSkinData skin, CheckResultCallback checkResultCallback, Executor checkThread) {
    }
}
