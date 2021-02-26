package com.particle.api.ai;

import java.io.InputStream;

public interface IAiConfigLoaderApi {
    void parseConfigData(InputStream configData) throws Exception;
}
