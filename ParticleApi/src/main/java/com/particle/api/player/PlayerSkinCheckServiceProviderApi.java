package com.particle.api.player;

import com.google.inject.Provider;

public interface PlayerSkinCheckServiceProviderApi extends Provider<PlayerSkinCheckServiceApi> {

    void registerPlayerSkinCheckService(PlayerSkinCheckServiceApi playerSkinCheckService);

}
