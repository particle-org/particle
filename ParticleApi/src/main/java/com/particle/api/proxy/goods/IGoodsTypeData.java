package com.particle.api.proxy.goods;

public interface IGoodsTypeData {
    /**
     * 获取商品类型
     *
     * @return 商品类型
     */
    String getType();

    /**
     * 获取商品名
     *
     * @return 商品名
     */
    String getName();

    /**
     * 获取商品描述
     *
     * @return 商品描述
     */
    String getDescription();

    /**
     * 获取该类商品最大堆叠数量
     *
     * @return 堆叠数量
     */
    int getMaxStack();
}
