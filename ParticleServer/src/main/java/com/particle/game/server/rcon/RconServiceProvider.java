package com.particle.game.server.rcon;

import com.particle.api.server.RconServiceApi;
import com.particle.api.server.RconServiceProviderApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RconServiceProvider implements RconServiceProviderApi {

    @Inject
    private DefaultRconService defaultRconService;

    private RconServiceApi rconService;

    @Override
    public RconServiceApi get() {
        return rconService == null ? defaultRconService : rconService;
    }

    @Override
    public void registerRconService(RconServiceApi rconService) {
        this.rconService = rconService;
    }
}
