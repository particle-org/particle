package com.particle.api.server;

import com.google.inject.Provider;

public interface RconServiceProviderApi extends Provider<RconServiceApi> {

    void registerRconService(RconServiceApi rconService);

}
