package com.particle.api.server;

import com.google.inject.Provider;

public interface PrisonServiceProviderApi extends Provider<PrisonServiceApi> {

    void registerPrisonService(PrisonServiceApi prisonService);

}
