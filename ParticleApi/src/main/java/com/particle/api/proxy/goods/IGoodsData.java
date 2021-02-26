package com.particle.api.proxy.goods;

public interface IGoodsData {
    /**
     * 获取商品唯一ID
     *
     * @return 商品唯一ID
     */
    String getId();

    /**
     * 获取商品标识
     *
     * @return 商品标识
     */
    String getProviderName();

    /**
     * 商品列表中点击回调方法
     *
     * @return 回调方法
     */
    GoodsClickHandler onClick();

    /**
     * 获取商品类型
     *
     * @return 商品类型
     */
    String getType();

    /**
     * 获取商品数量
     *
     * @return 商品数量
     */
    int getAmount();

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
     * 商品显示图标
     *
     * @return 图标
     */
    String getIcon();

    /**
     * 商品当前状态描述
     *
     * @return 状态描述
     */
    String getStatusDescription();
}
