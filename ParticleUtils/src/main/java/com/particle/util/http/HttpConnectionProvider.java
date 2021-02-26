package com.particle.util.http;

import com.google.inject.Provider;
import com.particle.util.http.impl.OKHttpImpl;

import javax.inject.Singleton;

@Singleton
public class HttpConnectionProvider implements Provider<IHttpConnectionApi> {
    @Override
    public IHttpConnectionApi get() {
        return new OKHttpImpl();
    }
}
