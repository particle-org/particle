package com.particle.api.proxy.goods;

public interface GoodsServiceApi {
    void registerProvider(IGoodsProvider provider);

    IGoodsProvider getProvider(String name);
}
