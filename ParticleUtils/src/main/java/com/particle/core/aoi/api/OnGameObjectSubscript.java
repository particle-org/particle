package com.particle.core.aoi.api;

import java.net.InetSocketAddress;

@FunctionalInterface
public interface OnGameObjectSubscript {
    boolean handle(InetSocketAddress object);
}
