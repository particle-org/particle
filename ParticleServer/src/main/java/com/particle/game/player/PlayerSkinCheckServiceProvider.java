package com.particle.game.player;

import com.google.inject.Singleton;
import com.particle.api.player.PlayerSkinCheckServiceApi;
import com.particle.api.player.PlayerSkinCheckServiceProviderApi;

import javax.inject.Inject;

@Singleton
public class PlayerSkinCheckServiceProvider implements PlayerSkinCheckServiceProviderApi {
    @Inject
    private DefaultPlayerSkinCheckService defaultPlayerSkinCheckService;

    private PlayerSkinCheckServiceApi playerSkinCheckService;

    @Override
    public void registerPlayerSkinCheckService(PlayerSkinCheckServiceApi playerSkinCheckService) {
        this.playerSkinCheckService = playerSkinCheckService;
    }

    @Override
    public PlayerSkinCheckServiceApi get() {
        return playerSkinCheckService == null ? defaultPlayerSkinCheckService : playerSkinCheckService;
    }
}
