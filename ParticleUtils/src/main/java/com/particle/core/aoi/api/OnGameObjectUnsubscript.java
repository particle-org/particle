package com.particle.core.aoi.api;

import java.net.InetSocketAddress;

@FunctionalInterface
public interface OnGameObjectUnsubscript {
    boolean handle(InetSocketAddress object);
}
