package com.particle.game.server.prison;

import com.particle.api.server.PrisonServiceApi;
import com.particle.api.server.PrisonServiceProviderApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrisonServiceProvider implements PrisonServiceProviderApi {
    @Inject
    private DefaultPrisonService defaultPrisonService;

    private PrisonServiceApi prisonService;

    @Override
    public PrisonServiceApi get() {
        return prisonService == null ? defaultPrisonService : prisonService;
    }

    @Override
    public void registerPrisonService(PrisonServiceApi prisonService) {
        this.prisonService = prisonService;
    }
}
